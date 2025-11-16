package com.example.wellnessapp.data.repository

import com.example.lunacare.data.dao.*
import com.example.lunacare.data.entities.*

class AppRepository(
    private val userDao: UserDao,
    private val moodDao: MoodDao,
    private val journalDao: JournalDao
) {
    // User Authentication
    suspend fun registerUser(user: User) = userDao.registerUser(user)
    suspend fun login(email: String, password: String) = userDao.login(email, password)

    // Mood Tracking
    suspend fun insertMood(mood: MoodEntry) = moodDao.insertMood(mood)
    suspend fun getUserMoods(userId: Int) = moodDao.getUserMoods(userId)

    // Journal Entries
    suspend fun insertJournal(journal: JournalEntry) = journalDao.insertJournal(journal)
    suspend fun getUserJournals(userId: Int) = journalDao.getUserJournals(userId)
}
