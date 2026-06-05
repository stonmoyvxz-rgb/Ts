package com.example.ui

import android.app.Application
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.Caption
import com.example.data.CaptionRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class Screen {
    object Home : Screen()
    object Categories : Screen()
    data class CategoryDetail(val categoryKey: String, val title: String) : Screen()
    data class CaptionDetail(val captionId: Int) : Screen()
    object AiGenerator : Screen()
    object Profile : Screen()
    object Admin : Screen()
}

class BhabkothaViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val repository = CaptionRepository(database.captionDao())

    // UI Screen navigation states
    private val _currentScreen = mutableStateOf<Screen>(Screen.Home)
    val currentScreen: State<Screen> = _currentScreen

    private val backstack = mutableListOf<Screen>(Screen.Home)

    // Flow states
    val allCaptions: StateFlow<List<Caption>> = repository.allCaptions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val featuredCaptions: StateFlow<List<Caption>> = repository.featuredCaptions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val likedCaptions: StateFlow<List<Caption>> = repository.likedCaptions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userSubmittedCaptions: StateFlow<List<Caption>> = repository.userSubmittedCaptions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pendingSubmissions: StateFlow<List<Caption>> = repository.pendingSubmissions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Search query using StateFlow for 100% reactive processing
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Filtered captions flow using combine over stateflows (eliminates snapshotFlow compiler errors)
    val filteredCaptions: StateFlow<List<Caption>> = allCaptions
        .combine(searchQuery) { captions, query ->
            if (query.isBlank()) {
                captions.filter { it.isApproved }
            } else {
                captions.filter {
                    it.isApproved && (
                        it.text.contains(query, ignoreCase = true) ||
                        it.author.contains(query, ignoreCase = true) ||
                        (it.meaning?.contains(query, ignoreCase = true) ?: false)
                    )
                }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // AI Generator states
    private val _isGenerating = mutableStateOf(false)
    val isGenerating: State<Boolean> = _isGenerating

    private val _generatedCaptions = mutableStateOf<List<Caption>>(emptyList())
    val generatedCaptions: State<List<Caption>> = _generatedCaptions

    private val _aiError = mutableStateOf<String?>(null)
    val aiError: State<String?> = _aiError

    init {
        viewModelScope.launch {
            try {
                repository.prepopulateIfEmpty()
            } catch (e: Exception) {
                Log.e("BhabkothaViewModel", "Prepopulate failed: ${e.message}")
            }
        }
    }

    // Navigation methods
    fun navigateTo(screen: Screen) {
        if (backstack.lastOrNull() != screen) {
            backstack.add(screen)
        }
        _currentScreen.value = screen
    }

    fun navigateBack(): Boolean {
        if (backstack.size > 1) {
            backstack.removeAt(backstack.lastIndex)
            _currentScreen.value = backstack.last()
            return true
        }
        return false // Exited app
    }

    fun clearBackToHome() {
        backstack.clear()
        backstack.add(Screen.Home)
        _currentScreen.value = Screen.Home
    }

    // Search management
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // Database Actions
    fun toggleLike(caption: Caption) {
        viewModelScope.launch {
            repository.updateLikeStatus(caption.id, !caption.isLiked)
            // If the user liked it and it's also in generatedCaptions, update in memory
            _generatedCaptions.value = _generatedCaptions.value.map {
                if (it.text == caption.text) it.copy(isLiked = !caption.isLiked) else it
            }
        }
    }

    fun saveGeneratedCaption(caption: Caption) {
        viewModelScope.launch {
            // Save newly generated AI caption locally to the database as approved
            val approvedCaption = caption.copy(isApproved = true, isLiked = true, timestamp = System.currentTimeMillis())
            val id = repository.insert(approvedCaption)
            
            // Update the generated state so it reflects as liked
            _generatedCaptions.value = _generatedCaptions.value.map {
                if (it.text == caption.text) it.copy(id = id.toInt(), isLiked = true) else it
            }
        }
    }

    fun submitUserCaption(text: String, author: String, category: String, meaning: String?) {
        viewModelScope.launch {
            val userCaption = Caption(
                text = text,
                author = if (author.isBlank()) "সংগৃহীত" else author,
                category = category,
                meaning = meaning,
                isUserSubmitted = true,
                isApproved = false, // Set to false so Admin must approve
                timestamp = System.currentTimeMillis()
            )
            repository.insert(userCaption)
        }
    }

    fun approveCaption(id: Int) {
        viewModelScope.launch {
            repository.approveCaption(id)
        }
    }

    fun deleteCaption(caption: Caption) {
        viewModelScope.launch {
            repository.delete(caption)
        }
    }

    // AI Caption Generator Flow
    fun generateCaptions(keyword: String, style: String) {
        _isGenerating.value = true
        _aiError.value = null
        _generatedCaptions.value = emptyList()

        viewModelScope.launch {
            try {
                val results = repository.generateCaptionsWithAI(keyword, style)
                _generatedCaptions.value = results
            } catch (e: Exception) {
                _aiError.value = e.message ?: "একটি অপরিচিত ত্রুটি ঘটেছে।"
                Log.e("BhabkothaViewModel", "AI generation failed", e)
            } finally {
                _isGenerating.value = false
            }
        }
    }

    fun clearAiCaptions() {
        _generatedCaptions.value = emptyList()
        _aiError.value = null
    }
}

// Multiplatform Top Level Instantiator Factory
class BhabkothaViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BhabkothaViewModel::class.java)) {
            return BhabkothaViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
