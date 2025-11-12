package com.example.lunacare.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lunacare.data.repository.AppRepository

class ViewModelFactory(private val repo: AppRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) ->
                AuthViewModel(repo) as T
            modelClass.isAssignableFrom(MoodViewModel::class.java) ->
                MoodViewModel(repo) as T
            modelClass.isAssignableFrom(JournalViewModel::class.java) ->
                JournalViewModel(repo) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
