package com.example.ui

import android.app.Application
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BhabkothaApp(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    
    // Shared ViewModel instantiation
    val viewModel: BhabkothaViewModel = viewModel(
        factory = BhabkothaViewModelFactory(application)
    )

    // Dark Mode Local UI Override State
    var isDarkTheme by remember { mutableStateOf(false) }

    val currentScreen by viewModel.currentScreen

    // Handle back button presses in Compose cleanly
    BackHandler(enabled = currentScreen != Screen.Home) {
        viewModel.navigateBack()
    }

    MyApplicationTheme(darkTheme = isDarkTheme) {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = {
                // Render custom header if not on full details screens
                if (currentScreen !is Screen.CaptionDetail && currentScreen !is Screen.CategoryDetail) {
                    CenterAlignedTopAppBar(
                        title = {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "ভাবকথা",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 24.sp,
                                    fontFamily = FontFamily.Serif,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "সাহিত্য ও দর্শনের উন্মুক্ত স্রোত",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.secondary,
                                    letterSpacing = 1.sp
                                )
                            }
                        },
                        actions = {
                            // Dark mode toggle in action bar
                            IconButton(
                                onClick = { isDarkTheme = !isDarkTheme },
                                modifier = Modifier.testTag("dark_mode_toggle")
                            ) {
                                Icon(
                                    imageVector = if (isDarkTheme) Icons.Default.Settings else Icons.Default.Settings, // Settings act as standard dark toggle or sun/moon representation
                                    contentDescription = "রঙিন মোড বদলান",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            scrolledContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                        )
                    )
                }
            },
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp),
                    windowInsets = WindowInsets.navigationBars,
                    modifier = Modifier.testTag("bottom_nav_bar")
                ) {
                    // Item 1: Home
                    NavigationBarItem(
                        selected = currentScreen is Screen.Home,
                        onClick = { viewModel.clearBackToHome() },
                        icon = { Icon(Icons.Default.Home, contentDescription = "হোম") },
                        label = { Text("হোম", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier.testTag("nav_item_home")
                    )

                    // Item 2: Categories
                    NavigationBarItem(
                        selected = currentScreen is Screen.Categories || currentScreen is Screen.CategoryDetail,
                        onClick = { viewModel.navigateTo(Screen.Categories) },
                        icon = { Icon(Icons.Default.Menu, contentDescription = "বিভাগ") },
                        label = { Text("বিভাগ", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier.testTag("nav_item_categories")
                    )

                    // Item 3: AI Generator
                    NavigationBarItem(
                        selected = currentScreen is Screen.AiGenerator,
                        onClick = { viewModel.navigateTo(Screen.AiGenerator) },
                        icon = { Icon(Icons.Default.Star, contentDescription = "এআই") },
                        label = { Text("ভাব-তরী (AI)", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier.testTag("nav_item_generator")
                    )

                    // Item 4: Profile & custom creations
                    NavigationBarItem(
                        selected = currentScreen is Screen.Profile,
                        onClick = { viewModel.navigateTo(Screen.Profile) },
                        icon = { Icon(Icons.Default.Person, contentDescription = "প্রোফাইল") },
                        label = { Text("ইউজার", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier.testTag("nav_item_profile")
                    )

                    // Item 5: Admin dashboard
                    NavigationBarItem(
                        selected = currentScreen is Screen.Admin,
                        onClick = { viewModel.navigateTo(Screen.Admin) },
                        icon = { Icon(Icons.Default.Settings, contentDescription = "ড্যাশবোর্ড") }, // using Settings as admin representation
                        label = { Text("অ্যাডমিন", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier.testTag("nav_item_admin")
                    )
                }
            }
        ) { innerPadding ->
            // Switcher container with sleek transition animation
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                },
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                label = "screenAnimation"
            ) { screen ->
                when (screen) {
                    is Screen.Home -> {
                        HomeScreen(
                            viewModel = viewModel,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                    is Screen.Categories -> {
                        CategoriesScreen(
                            viewModel = viewModel,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                    is Screen.CategoryDetail -> {
                        // CategoryDetailScreen has its own scaffold, so pass padding carefully inside
                        CategoryDetailScreen(
                            categoryKey = screen.categoryKey,
                            title = screen.title,
                            viewModel = viewModel
                        )
                    }
                    is Screen.CaptionDetail -> {
                        // DetailScreen has its own scaffold
                        DetailScreen(
                            captionId = screen.captionId,
                            viewModel = viewModel
                        )
                    }
                    is Screen.AiGenerator -> {
                        AiGeneratorScreen(
                            viewModel = viewModel,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                    is Screen.Profile -> {
                        ProfileScreen(
                            viewModel = viewModel,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                    is Screen.Admin -> {
                        AdminScreen(
                            viewModel = viewModel,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}
