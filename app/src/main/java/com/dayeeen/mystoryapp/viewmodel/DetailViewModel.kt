package com.dayeeen.mystoryapp.viewmodel

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

    fun fetchDetail(id: String) {
        viewModelScope.launch {
            try {
                val detailResponse = repository.getDetail(id)
                _detail.postValue(detailResponse)
            } catch (_: Exception) {
            }
        }
    }
}