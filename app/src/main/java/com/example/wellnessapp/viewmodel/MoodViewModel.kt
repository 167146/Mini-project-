package com.example.wellnessapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wellnessapp.data.entities.MoodEntry
import com.example.wellnessapp.data.repository.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MoodViewModel(private val repository: AppRepository) : ViewModel() {

    private val _moods = MutableStateFlow<List<MoodEntry>>(emptyList())
    val moods: StateFlow<List<MoodEntry>> get() = _moods

    fun loadMoods(userId: Int) {
        viewModelScope.launch {
            repository.getUserMoods(userId).collect { moodList ->
                _moods.value = moodList
            }
        }
    }

    fun addMood(mood: MoodEntry) {
        viewModelScope.launch {
            repository.insertMood(mood)
            loadMoods(mood.userId) // optional: flow will update automatically
        }
    }
}
