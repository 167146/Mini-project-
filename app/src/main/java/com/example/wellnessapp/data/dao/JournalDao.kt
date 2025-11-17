package com.example.wellnessapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.wellnessapp.data.entities.JournalEntry

@Dao
interface JournalDao {

    @Insert
    suspend fun insertJournal(journal: JournalEntry)

    @Query("SELECT * FROM journal_entries WHERE userId = :userId ORDER BY date DESC")
    fun getUserJournals(userId: Int): kotlinx.coroutines.flow.Flow<List<JournalEntry>>
}

