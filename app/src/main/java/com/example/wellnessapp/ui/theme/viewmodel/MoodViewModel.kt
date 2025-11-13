package com.example.lunacare.ui.viewmodel

import androidx.lifecycle.*
import com.example.lunacare.data.entities.MoodEntry
import com.example.lunacare.data.repository.AppRepository
import kotlinx.coroutines.launch

class MoodViewModel(private val repo: AppRepository) : ViewModel() {

    private val _moods = MutableLiveData<List<MoodEntry>>()
    val moods: LiveData<List<MoodEntry>> get() = _moods

    fun addMood(mood: MoodEntry) {
        viewModelScope.launch {
            repo.insertMood(mood)
            _moods.value = repo.getUserMoods(mood.userId)
        }
    }

    fun getMoods(userId: Int) {
        viewModelScope.launch {
            _moods.value = repo.getUserMoods(userId)
        }
    }
}
