package com.dayeeen.mystoryapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dayeeen.mystoryapp.data.repository.UserRepository
import com.dayeeen.mystoryapp.data.response.StoryResponse
import kotlinx.coroutines.launch

class MapsViewModel(private val repository: UserRepository) : ViewModel() {
    private val _stories = MutableLiveData<StoryResponse>()
    val stories: LiveData<StoryResponse> get() = _stories

    init {
        fetchStoriesWithLocation()
    }

    private fun fetchStoriesWithLocation() {
        viewModelScope.launch {
            try {
                val response = repository.getStoriesWithLocation()
                _stories.value = response
            } catch (e: Exception) {
                Log.d(mapsViewModel, "Error getting stories with location: ${e.message}", e)
            }
        }
    }

    companion object {
        private const val mapsViewModel = "MapsViewModel"
    }
}