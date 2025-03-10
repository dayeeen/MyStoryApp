package com.dayeeen.mystoryapp.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dayeeen.mystoryapp.data.repository.UserRepository
import com.dayeeen.mystoryapp.data.response.ListStoryItem
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: UserRepository) : ViewModel() {

    private val _detail = MutableLiveData<ListStoryItem>()
    val detail: LiveData<ListStoryItem> get() = _detail

    fun fetchDetail(context: Context, id: String) {
        viewModelScope.launch {
            try {
                val detailResponse = repository.getDetail(id)
                _detail.postValue(detailResponse)
            } catch (e: Exception) {
                // Display a Toast message to the user
                Toast.makeText(context, "Failed to fetch story details", Toast.LENGTH_SHORT).show()
            }
        }
    }
}