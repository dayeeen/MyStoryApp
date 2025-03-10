package com.dayeeen.mystoryapp.data.di

import android.content.Context
import com.dayeeen.mystoryapp.data.database.StoryDatabase
import com.dayeeen.mystoryapp.data.preferences.UserPreference
import com.dayeeen.mystoryapp.data.preferences.dataStore
import com.dayeeen.mystoryapp.data.repository.UserRepository
import com.dayeeen.mystoryapp.data.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        val storyDatabase = StoryDatabase.getDatabase(context)
        return UserRepository.getInstance(apiService, pref, storyDatabase)
    }
}