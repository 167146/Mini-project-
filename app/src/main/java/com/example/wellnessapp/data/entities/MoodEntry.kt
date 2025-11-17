package com.example.wellnessapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "moods")
data class MoodEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val mood: String,
    val timestamp: Long
)
