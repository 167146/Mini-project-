package com.example.lunacare.data.dao

import androidx.room.*
import com.example.lunacare.data.entities.JournalEntry

@Dao
interface JournalDao {

    @Insert
    suspend fun insertJournal(journal: JournalEntry)

    @Query("SELECT * FROM journal_entries WHERE userId = :userId ORDER BY date DESC")
    suspend fun getUserJournals(userId: Int): List<JournalEntry>
}
