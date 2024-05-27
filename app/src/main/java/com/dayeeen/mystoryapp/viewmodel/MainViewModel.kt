package com.dayeeen.mystoryapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dayeeen.mystoryapp.data.UserData
import com.dayeeen.mystoryapp.data.repository.UserRepository
import com.dayeeen.mystoryapp.data.response.ListStoryItem
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {

    private val _stories = MutableLiveData<List<ListStoryItem>>()
    val stories: LiveData<List<ListStoryItem>> get() = _stories

    init {
        getStories()
    }

    private fun getStories() {
        viewModelScope.launch {
            try {
                val stories = repository.getStories()
                _stories.postValue(stories)
            } catch (_: Exception) {
            }
        }
    }

    fun getSession(): LiveData<UserData> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.clearSession()
        }
    }

}