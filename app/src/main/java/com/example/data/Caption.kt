package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "captions")
data class Caption(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val text: String,
    val category: String,
    val author: String = "অপরিচিত",
    val meaning: String? = null,
    val isLiked: Boolean = false,
    val isFeatured: Boolean = false,
    val isUserSubmitted: Boolean = false,
    val isApproved: Boolean = true,
    val timestamp: Long = System.currentTimeMillis()
)
