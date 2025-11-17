package com.example.wellnessapp.data.repository

import com.example.wellnessapp.data.dao.MoodDao
import com.example.wellnessapp.data.dao.JournalDao
import com.example.wellnessapp.data.dao.UserDao
import com.example.wellnessapp.data.entities.MoodEntry
import com.example.wellnessapp.data.entities.JournalEntry
import com.example.wellnessapp.data.entities.User
import kotlinx.coroutines.flow.Flow

class AppRepository(
    private val userDao: UserDao,
    private val moodDao: MoodDao,
    private val journalDao: JournalDao
) {
    // User
    suspend fun registerUser(user: User) = userDao.registerUser(user)
    suspend fun login(email: String, password: String) = userDao.login(email, password)
    suspend fun getUserByEmail(email: String) = userDao.getUserByEmail(email)

    // Mood
    suspend fun insertMood(mood: MoodEntry) = moodDao.insertMood(mood)
    fun getUserMoods(userId: Int): Flow<List<MoodEntry>> = moodDao.getUserMoods(userId)

    // Journal
    suspend fun insertJournal(journal: JournalEntry) = journalDao.insertJournal(journal)
    fun getUserJournals(userId: Int): Flow<List<JournalEntry>> = journalDao.getUserJournals(userId)
}
