package com.dayeeen.mystoryapp.viewmodel

import androidx.lifecycle.ViewModel
import com.dayeeen.mystoryapp.data.repository.UserRepository
import java.io.File

class UploadStoryViewModel(private val repository: UserRepository) : ViewModel() {
    fun uploadStory(file: File, description: String) = repository.uploadStory(file, description)
}