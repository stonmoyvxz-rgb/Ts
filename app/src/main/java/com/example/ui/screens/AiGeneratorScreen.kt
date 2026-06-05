package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Caption
import com.example.ui.BhabkothaViewModel
import com.example.ui.Screen
import kotlinx.coroutines.delay

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AiGeneratorScreen(
    viewModel: BhabkothaViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var keyword by remember { mutableStateOf("") }
    var selectedStyle by remember { mutableStateOf("Spiritual") }

    val isGenerating by viewModel.isGenerating
    val generatedCaptions by viewModel.generatedCaptions
    val aiError by viewModel.aiError

    val stylesList = remember {
        listOf(
            "Spiritual" to "আধ্যাত্মিক",
            "Emotional" to "আবেগীয়",
            "Inspirational" to "অনুপ্রেরণামূলক",
            "Minimalist" to "স্বল্পভাষী"
        )
    }

    // Interactive custom literature taglines cycled during loading state
    var loadingPhraseIndex by remember { mutableStateOf(0) }
    val loadingPhrases = remember {
        listOf(
            "ভাব গভীরতার খোঁজ চলছে...",
            "শব্দের সুতোয় দর্শন বোনা হচ্ছে...",
            "কাগজে কালির আঁচড় সাজানো হচ্ছে...",
            "সাহিত্যিক উপমার মেলবন্ধন খোঁজা হচ্ছে...",
            "ভাবকথা আপনার অপেক্ষায় সমৃদ্ধ হচ্ছে..."
        )
    }

    LaunchedEffect(isGenerating) {
        if (isGenerating) {
            loadingPhraseIndex = 0
            while (isGenerating) {
                delay(2400)
                loadingPhraseIndex = (loadingPhraseIndex + 1) % loadingPhrases.size
            }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("ai_generator_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // App header intro
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "এআই ভাব-তরী (AI Caption Generator)",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
                Text(
                    text = "যেকোনো শব্দ লিখুন ও পছন্দসই সাহিত্যিক ধাঁচ বেছে নিয়ে লাভ করুন গভীরতম ভাবার্থপূর্ণ বাঙালি ক্যাপশনমালা।",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Input card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "১. শব্দ বা মূল বিষয় নির্ধারণ করুন",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    )

                    OutlinedTextField(
                        value = keyword,
                        onValueChange = { keyword = it },
                        modifier = Modifier.fillMaxWidth().testTag("ai_keyword_input"),
                        placeholder = { Text("যেমন: মায়া, বৃষ্টি, আকাশ, দুঃখ, একা...") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                        )
                    )

                    Text(
                        text = "২. লেখার সাহিত্যিক ধাঁচ সিলেক্ট করুন",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    )

                    // Flows of buttons
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        stylesList.forEach { (key, display) ->
                            val isSelected = selectedStyle == key
                            Surface(
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier
                                    .clickable { selectedStyle = key }
                                    .border(
                                        width = 1.dp,
                                        color = if (isSelected) Color.Transparent else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                    .testTag("style_button_$key")
                            ) {
                                Text(
                                    text = display,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Generation button
                    Button(
                        onClick = {
                            if (keyword.isBlank()) {
                                Toast.makeText(context, "অনুগ্রহ করে একটি শব্দ লিখুন!", Toast.LENGTH_SHORT).show()
                            } else {
                                viewModel.generateCaptions(keyword, selectedStyle)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("ai_generate_button"),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isGenerating
                    ) {
                        if (isGenerating) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White,
                                strokeWidth = 2.5.dp
                            )
                        } else {
                            Icon(Icons.Default.Star, contentDescription = null, modifier = Modifier.padding(end = 6.dp))
                            Text("এআই ক্যাপশন তৈরি করুন", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Error message banner
        if (aiError != null) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "ত্রুটি ঘটেছে!",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onErrorContainer)
                        )
                        Text(
                            text = aiError!!,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            textAlign = TextAlign.Center
                        )
                        
                        // Suggest user configuration
                        Text(
                            text = "পরামর্শ: গুগল এআই স্টুডিও সিক্রেটস প্যানেলে GEMINI_API_KEY সঠিকভাবে সেট করা আছে কিনা অনুগ্রহ করে নিশ্চিত করুন।",
                            style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic),
                            color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.8f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        // Generating / Shimmer loader screen state
        if (isGenerating) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(48.dp)
                        )
                        
                        AnimatedContent(
                            targetState = loadingPhrases[loadingPhraseIndex],
                            transitionSpec = {
                                fadeIn() + slideInVertically() togetherWith fadeOut() + slideOutVertically()
                            },
                            label = "loadingPhrase"
                        ) { phrase ->
                            Text(
                                text = phrase,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                ),
                                fontStyle = FontStyle.Italic,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }

        // Generated Captions List header
        if (generatedCaptions.isNotEmpty() && !isGenerating) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "তৈরিকৃত ভাবকথা",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    )
                    TextButton(onClick = { viewModel.clearAiCaptions() }) {
                        Text("মুছে ফেলুন", color = MaterialTheme.colorScheme.error)
                    }
                }
            }

            // Results render loop
            items(generatedCaptions) { caption ->
                AiCaptionResultCard(
                    caption = caption,
                    onSaveClick = {
                        viewModel.saveGeneratedCaption(caption)
                        Toast.makeText(context, "সংগ্রহশালায় ও ফেভারিটে ধারণ করা হয়েছে!", Toast.LENGTH_SHORT).show()
                    },
                    onCopyClick = {
                        com.example.ui.screens.copyCaptionText(context, caption.text, caption.author)
                    },
                    onShareClick = {
                        com.example.ui.screens.shareCaptionText(context, caption.text, caption.author)
                    }
                )
            }
        }
    }
}

@Composable
fun AiCaptionResultCard(
    caption: Caption,
    onSaveClick: () -> Unit,
    onCopyClick: () -> Unit,
    onShareClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("ai_result_card"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "“${caption.text}”",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 17.sp,
                    lineHeight = 26.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            if (!caption.meaning.isNullOrBlank()) {
                Text(
                    text = "অনুরণন: ${caption.meaning}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.08f), RoundedCornerShape(4.dp))
                        .padding(8.dp)
                )
            }

            Divider(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "— ${caption.author}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.9f)
                    )
                )

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    // Copy
                    IconButton(onClick = onCopyClick, modifier = Modifier.size(36.dp)) {
                        Icon(
                            imageVector = Icons.Default.Add, // representation of clipboard copy
                            contentDescription = "কপি করুন",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Share
                    IconButton(onClick = onShareClick, modifier = Modifier.size(36.dp)) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "শেয়ার করুন",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Save / Bookmark locally
                    Button(
                        onClick = onSaveClick,
                        enabled = !caption.isLiked, // Disabled if already bookmarked
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Icon(
                            imageVector = if (caption.isLiked) Icons.Default.Check else Icons.Default.Favorite,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (caption.isLiked) "সংরক্ষিত" else "সংরক্ষণ", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
