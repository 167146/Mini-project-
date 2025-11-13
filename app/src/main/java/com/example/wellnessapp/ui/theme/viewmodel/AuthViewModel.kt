package com.example.lunacare.ui.viewmodel

import androidx.lifecycle.*
import com.example.lunacare.data.entities.User
import com.example.lunacare.data.repository.AppRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val repo: AppRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<User?>()
    val loginResult: LiveData<User?> get() = _loginResult

    fun registerUser(user: User) {
        viewModelScope.launch {
            repo.registerUser(user)
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginResult.value = repo.login(email, password)
        }
    }
}
