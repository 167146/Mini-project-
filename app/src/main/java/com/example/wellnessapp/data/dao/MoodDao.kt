package com.example.wellnessapp.data.dao

import androidx.room.*
import com.example.wellnessapp.data.entities.MoodEntry

@Dao
interface MoodDao {

    @Insert
    suspend fun insertMood(mood: MoodEntry)

    @Query("SELECT * FROM moods WHERE userId = :userId ORDER BY timestamp DESC")
    fun getUserMoods(userId: Int): kotlinx.coroutines.flow.Flow<List<MoodEntry>>
}

