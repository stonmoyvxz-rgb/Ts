package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.window.Dialog
import com.example.data.Caption
import com.example.ui.BhabkothaViewModel
import com.example.ui.Screen

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileScreen(
    viewModel: BhabkothaViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val likedCaptions by viewModel.likedCaptions.collectAsState()
    val userSubmittedCaptions by viewModel.userSubmittedCaptions.collectAsState()

    var showSubmitDialog by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) } // 0: Liked, 1: Submitted

    // Active users credentials from local systems metadata
    val userEmail = "stonmoyvxz@gmail.com"
    val userName = "সৌম্য তন্ময় (Stonmoy)"
    val userRole = "শব্দ সাধক ও ভাব সংকলক"

    val categories = remember {
        listOf(
            "Spirituality" to "আধ্যাত্মিকতা",
            "Life Philosophy" to "জীবন দর্শন",
            "Love & Emotions" to "প্রেম ও অনুভূতি",
            "Motivation" to "অনুপ্রেরণা",
            "Human Values" to "মানবিক মূল্যবোধ",
            "Word Meaning Analysis" to "শব্দার্থ বিশ্লেষণ"
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("profile_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. User profile layout header card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Custom literary initials avatar
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "ত", // First letter of Tanmoy in Bengali
                            color = Color.White,
                            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                            fontFamily = FontFamily.Serif
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = userName,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        )
                        Surface(
                            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.08f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = userRole,
                                color = MaterialTheme.colorScheme.secondary,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = userEmail,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f), modifier = Modifier.padding(vertical = 4.dp))

                    // "Submit custom caption" button trigger
                    Button(
                        onClick = { showSubmitDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("submit_caption_button"),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.padding(end = 4.dp))
                        Text("নতুন ভাবকথা জমা দিন (Submit)", fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        // 2. Interactive Navigation tabs: Saved vs My custom submissions
        item {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.primary,
                divider = { Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)) }
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("প্রিয় ভাবমালা (${likedCaptions.size})", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("আমার সংগ্রহ (${userSubmittedCaptions.size})", fontWeight = FontWeight.Bold) }
                )
            }
        }

        // Active listing corresponding to selection
        if (selectedTab == 0) {
            // Case A: Bookmarked captives
            if (likedCaptions.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "কোনো বাণী প্রিয় তালিকায় রাখা হয়নি। ক্যাপশনে হার্ট (❤️) চিহ্ন স্পর্শ করলেই তা সংরক্ষিত হবে।",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.outline,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                items(likedCaptions, key = { "liked_${it.id}" }) { caption ->
                    CaptionCard(
                        caption = caption,
                        onClick = { viewModel.navigateTo(Screen.CaptionDetail(caption.id)) },
                        onLikeClick = { viewModel.toggleLike(caption) },
                        onShareClick = {
                            com.example.ui.screens.shareCaptionText(context, caption.text, caption.author)
                        }
                    )
                }
            }
        } else {
            // Case B: Submitted captives
            if (userSubmittedCaptions.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "আপনি এখনো কোনো ভাবকথা বা সাহিত্যিক উদ্ধৃতি নিজ হাতে লিখেননি। উপরের বাটনে ক্লিক করে প্রথম জমাটি দিন!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.outline,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                items(userSubmittedCaptions, key = { "submitted_${it.id}" }) { caption ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.navigateTo(Screen.CaptionDetail(caption.id)) },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = getBanglaCategoryName(caption.category),
                                        color = MaterialTheme.colorScheme.secondary,
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    if (caption.isApproved) {
                                        Icon(Icons.Default.Check, contentDescription = "অনুমোদিত", tint = Color(0xFF2E8B57), modifier = Modifier.size(16.dp))
                                        Text("অনুমোদিত", color = Color(0xFF2E8B57), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                                    } else {
                                        Icon(Icons.Default.Info, contentDescription = "অপেক্ষমান", tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(16.dp))
                                        Text("পর্যবেক্ষনে", color = MaterialTheme.colorScheme.secondary, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }

                            Text(
                                text = caption.text,
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp, fontFamily = FontFamily.Serif),
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            if (!caption.meaning.isNullOrBlank()) {
                                Text(
                                    text = "ব্যাখ্যা: ${caption.meaning}",
                                    style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f))
                                )
                            }

                            Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.05f))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "— ${caption.author}",
                                    style = MaterialTheme.typography.titleSmall.copy(fontSize = 12.sp, fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )

                                IconButton(onClick = { viewModel.deleteCaption(caption) }, modifier = Modifier.size(24.dp)) {
                                    Icon(Icons.Default.Delete, contentDescription = "মুছে ফেলুন", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal popup dialog containing custom submissions form
    if (showSubmitDialog) {
        var textValue by remember { mutableStateOf("") }
        var authorValue by remember { mutableStateOf("") }
        var activeCategory by remember { mutableStateOf("Spirituality") }
        var explanationValue by remember { mutableStateOf("") }

        Dialog(onDismissRequest = { showSubmitDialog = false }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                LazyColumn(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 8.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "উদ্ধৃতি বা ভাবকথা জমা দিন",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            )
                            IconButton(onClick = { showSubmitDialog = false }, modifier = Modifier.size(24.dp)) {
                                Icon(Icons.Default.Delete, contentDescription = "বন্ধ করুন", tint = MaterialTheme.colorScheme.outline) // delete serving as close
                            }
                        }
                    }

                    item {
                        Text(text = "১. মূল ভাবকথা বা ক্যাপশন (বাংলায়)", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                        OutlinedTextField(
                            value = textValue,
                            onValueChange = { textValue = it },
                            placeholder = { Text("বাণী বা চিন্তামালাটি এখানে লিখুন...") },
                            modifier = Modifier.fillMaxWidth().height(100.dp).testTag("custom_caption_input"),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MaterialTheme.colorScheme.primary)
                        )
                    }

                    item {
                        Text(text = "২. প্রকৃত রচয়িতা বা লেখক", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                        OutlinedTextField(
                            value = authorValue,
                            onValueChange = { authorValue = it },
                            placeholder = { Text("যেমন: রবীন্দ্রনাথ ঠাকুর বা সংগৃহীত...") },
                            modifier = Modifier.fillMaxWidth().testTag("custom_author_input"),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MaterialTheme.colorScheme.primary)
                        )
                    }

                    item {
                        Text(text = "৩. বিভাগ নির্বাচন করুন", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            categories.forEach { (key, display) ->
                                val isSelected = activeCategory == key
                                Surface(
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                    contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                    shape = RoundedCornerShape(20.dp),
                                    modifier = Modifier.clickable { activeCategory = key }
                                ) {
                                    Text(
                                        text = display,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold)
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Text(text = "৪. বাণীর নিহিত অর্থ বা ব্যাখ্যা (ঐচ্ছিক)", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                        OutlinedTextField(
                            value = explanationValue,
                            onValueChange = { explanationValue = it },
                            placeholder = { Text("উদ্ধৃতিটির সহজ ভাবার্থ ও অন্তরালের ব্যাখ্যা...") },
                            modifier = Modifier.fillMaxWidth().height(80.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MaterialTheme.colorScheme.primary)
                        )
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = { showSubmitDialog = false },
                                modifier = Modifier.weight(1f),
                                id = "cancel_button"
                            ) {
                                Text("বাতিল")
                            }

                            Button(
                                onClick = {
                                    if (textValue.isBlank()) {
                                        Toast.makeText(context, "অনুগ্রহ করে বাণীর ঘরটি ফাঁকা রাখবেন না!", Toast.LENGTH_SHORT).show()
                                    } else {
                                        viewModel.submitUserCaption(
                                            text = textValue,
                                            author = authorValue,
                                            category = activeCategory,
                                            meaning = explanationValue.ifBlank { null }
                                        )
                                        showSubmitDialog = false
                                        Toast.makeText(context, "সাবমিট সফল! অ্যাডমিন অনুমোদনের পর উন্মুক্ত স্রোতে প্রকাশিত হবে।", Toast.LENGTH_LONG).show()
                                    }
                                },
                                modifier = Modifier.weight(1.5f).testTag("custom_submit_confirm_button")
                            ) {
                                Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("জমা দিন", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

// Custom wrapper button extension allowing ID mappings
@Composable
fun OutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    id: String = "",
    content: @Composable RowScope.() -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.testTag(id),
        content = content
    )
}
