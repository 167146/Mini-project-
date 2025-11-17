package com.example.wellnessapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.wellnessapp.data.dao.JournalDao
import com.example.wellnessapp.data.dao.MoodDao
import com.example.wellnessapp.data.dao.UserDao
import com.example.wellnessapp.data.entities.JournalEntry
import com.example.wellnessapp.data.entities.MoodEntry
import com.example.wellnessapp.data.entities.User

@Database(
    entities = [User::class, MoodEntry::class, JournalEntry::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun moodDao(): MoodDao
    abstract fun journalDao(): JournalDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "luna_care_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
