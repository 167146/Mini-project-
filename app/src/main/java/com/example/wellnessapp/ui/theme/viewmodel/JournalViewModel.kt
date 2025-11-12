package com.example.lunacare.ui.viewmodel

import androidx.lifecycle.*
import com.example.lunacare.data.entities.JournalEntry
import com.example.lunacare.data.repository.AppRepository
import kotlinx.coroutines.launch

class JournalViewModel(private val repo: AppRepository) : ViewModel() {

    private val _journals = MutableLiveData<List<JournalEntry>>()
    val journals: LiveData<List<JournalEntry>> get() = _journals

    fun addJournal(journal: JournalEntry) {
        viewModelScope.launch {
            repo.insertJournal(journal)
            _journals.value = repo.getUserJournals(journal.userId)
        }
    }

    fun getJournals(userId: Int) {
        viewModelScope.launch {
            _journals.value = repo.getUserJournals(userId)
        }
    }
}
