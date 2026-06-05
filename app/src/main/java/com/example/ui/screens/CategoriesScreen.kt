package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Caption
import com.example.ui.BhabkothaViewModel
import com.example.ui.Screen

data class CategoryMeta(
    val key: String,
    val banglaName: String,
    val description: String,
    val colorGradient: List<Color>
)

@Composable
fun CategoriesScreen(
    viewModel: BhabkothaViewModel,
    modifier: Modifier = Modifier
) {
    val allCaptions by viewModel.allCaptions.collectAsState()

    // Definition of categories with customized warm literary gradients
    val categories = remember {
        listOf(
            CategoryMeta(
                key = "Spirituality",
                banglaName = "আধ্যাত্মিকতা",
                description = "আত্মানুসন্ধান, স্রষ্টা ও সৃষ্টির চিরন্তন রহস্যের মেলবন্ধন।",
                colorGradient = listOf(Color(0xFF8C2531), Color(0xFFB55D67))
            ),
            CategoryMeta(
                key = "Life Philosophy",
                banglaName = "জীবন दर्शन",
                description = "জীবনযাত্রার পথচলার সত্য ও গভীর জীবনবোধের অন্বেষণ।",
                colorGradient = listOf(Color(0xFF1E3A5F), Color(0xFF4A6B94))
            ),
            CategoryMeta(
                key = "Love & Emotions",
                banglaName = "প্রেম ও অনুভূতি",
                description = "হৃদয়ের না বলা আবেগ, অভিমান ও গভীর প্রেমের উপাখ্যান।",
                colorGradient = listOf(Color(0xFF6A0DAD), Color(0xFFA0522D))
            ),
            CategoryMeta(
                key = "Motivation",
                banglaName = "অনুপ্রেরণা",
                description = "হতাশা কাটিয়ে নতুন করে পথ চলার অদম্য সাহস ও শক্তি।",
                colorGradient = listOf(Color(0xFFB58436), Color(0xFFD4AF37))
            ),
            CategoryMeta(
                key = "Human Values",
                banglaName = "মানবিক মূল্যবোধ",
                description = "নৈতিকতা, সহমর্মিতা ও মানুষের শ্রেষ্ঠত্ব প্রতিষ্ঠার বাণী।",
                colorGradient = listOf(Color(0xFF2E8B57), Color(0xFF3CB371))
            ),
            CategoryMeta(
                key = "Word Meaning Analysis",
                banglaName = "शब्दार्थ विश्लेषण",
                description = "বাঙালি অভিধানের গভীর সাহিত্যিক শব্দের রূপ ও অর্থ।",
                colorGradient = listOf(Color(0xFF708090), Color(0xFF778899))
            )
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // App header intro
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "ভাবের ভাগাভাগি ও বিভাগ",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )
            Text(
                text = "আপনার প্রিয় বিষয়বস্তু অনুযায়ী গভীর সাহিত্যিক অনুভূতি খুঁজুন...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Beautiful Grid mapping layout
        LazyVerticalGrid(
            columns = GridCells.Fixed(1), // Use beautiful vertical 1-columns containing rich detailed lists
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(categories) { category ->
                val count = allCaptions.count { it.category == category.key && it.isApproved }
                CategoryGridCard(
                    category = category,
                    count = count,
                    onClick = { viewModel.navigateTo(Screen.CategoryDetail(category.key, category.banglaName)) }
                )
            }
        }
    }
}

@Composable
fun CategoryGridCard(
    category: CategoryMeta,
    count: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("category_card_${category.key}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Gradient Icon Indicator Circle
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Brush.linearGradient(category.colorGradient)),
                contentAlignment = Alignment.Center
            ) {
                // Render custom iconic representation based on category keys
                val icon = when (category.key) {
                    "Spirituality" -> "ধ"
                    "Life Philosophy" -> "জী"
                    "Love & Emotions" -> "হৃ"
                    "Motivation" -> "জ"
                    "Human Values" -> "মা"
                    else -> "শব্দ"
                }
                Text(
                    text = icon,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Serif
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = category.banglaName,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Badge(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    ) {
                        Text(
                            text = "${count}টি বাণী",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                Text(
                    text = category.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDetailScreen(
    categoryKey: String,
    title: String,
    viewModel: BhabkothaViewModel,
    modifier: Modifier = Modifier
) {
    val allCaptions by viewModel.allCaptions.collectAsState()
    val categoryCaptions = remember(allCaptions, categoryKey) {
        allCaptions.filter { it.category == categoryKey && it.isApproved }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "ফিরে যান")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        if (categoryCaptions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "এই বিভাগে এখনো কোনো ভাবকথা যুক্ত করা হয়নি।",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .testTag("category_detail_list"),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(categoryCaptions, key = { it.id }) { caption ->
                    CaptionCard(
                        caption = caption,
                        onClick = { viewModel.navigateTo(Screen.CaptionDetail(caption.id)) },
                        onLikeClick = { viewModel.toggleLike(caption) },
                        onShareClick = {
                            // Defined in HomeScreen
                            com.example.ui.screens.shareCaptionText(
                                context = viewModel.getApplication(),
                                text = caption.text,
                                author = caption.author
                            )
                        }
                    )
                }
            }
        }
    }
}
