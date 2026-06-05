package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CaptionDao {
    @Query("SELECT * FROM captions ORDER BY timestamp DESC")
    fun getAllCaptions(): Flow<List<Caption>>

    @Query("SELECT * FROM captions WHERE id = :id LIMIT 1")
    fun getCaptionById(id: Int): Flow<Caption?>

    @Query("SELECT * FROM captions WHERE category = :category AND isApproved = 1 ORDER BY timestamp DESC")
    fun getCaptionsByCategory(category: String): Flow<List<Caption>>

    @Query("SELECT * FROM captions WHERE isLiked = 1 ORDER BY timestamp DESC")
    fun getLikedCaptions(): Flow<List<Caption>>

    @Query("SELECT * FROM captions WHERE isFeatured = 1 AND isApproved = 1 ORDER BY timestamp DESC")
    fun getFeaturedCaptions(): Flow<List<Caption>>

    @Query("SELECT * FROM captions WHERE isUserSubmitted = 1 ORDER BY timestamp DESC")
    fun getUserSubmittedCaptions(): Flow<List<Caption>>

    @Query("SELECT * FROM captions WHERE isApproved = 0 ORDER BY timestamp DESC")
    fun getPendingSubmissions(): Flow<List<Caption>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCaption(caption: Caption): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCaptions(captions: List<Caption>)

    @Update
    suspend fun updateCaption(caption: Caption)

    @Delete
    suspend fun deleteCaption(caption: Caption)

    @Query("UPDATE captions SET isLiked = :isLiked WHERE id = :id")
    suspend fun updateLikeStatus(id: Int, isLiked: Boolean)

    @Query("UPDATE captions SET isApproved = 1 WHERE id = :id")
    suspend fun approveCaption(id: Int)

    @Query("SELECT COUNT(*) FROM captions")
    suspend fun getCount(): Int
}
