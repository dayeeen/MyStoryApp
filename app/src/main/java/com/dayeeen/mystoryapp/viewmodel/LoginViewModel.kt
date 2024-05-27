package com.dayeeen.mystoryapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dayeeen.mystoryapp.data.UserData
import com.dayeeen.mystoryapp.data.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    fun login(email: String, password: String) = repository.login(email, password)

    fun saveSession(user: UserData) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}
