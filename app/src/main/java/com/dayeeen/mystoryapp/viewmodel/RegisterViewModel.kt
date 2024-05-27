package com.dayeeen.mystoryapp.viewmodel

import androidx.lifecycle.ViewModel
import com.dayeeen.mystoryapp.data.repository.UserRepository

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {

    fun registerUser(name: String, email: String, password: String) =
        repository.register(name, email, password)
}
