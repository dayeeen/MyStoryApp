package com.dayeeen.mystoryapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dayeeen.mystoryapp.data.UserData
import com.dayeeen.mystoryapp.data.database.StoryDatabase
import com.dayeeen.mystoryapp.data.preferences.UserPreference
import com.dayeeen.mystoryapp.data.remote.StoryRemoteMediator
import com.dayeeen.mystoryapp.data.response.ListStoryItem
import com.dayeeen.mystoryapp.data.response.LoginResponse
import com.dayeeen.mystoryapp.data.response.RegisterResponse
import com.dayeeen.mystoryapp.data.response.StoryResponse
import com.dayeeen.mystoryapp.data.retrofit.ApiService
import com.dayeeen.mystoryapp.utils.StateResult
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class UserRepository private constructor(
    private var apiService: ApiService,
    private val userPreference: UserPreference,
    private val storyDatabase: StoryDatabase
) {


    fun updateApiService(apiService: ApiService) {
        this.apiService = apiService
    }

    suspend fun saveSession(user: UserData) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserData> {
        return userPreference.getSession()
    }

    fun getStoriesPaging(): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class) return Pager(config = PagingConfig(
            pageSize = 5
        ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStories()
            }).liveData
    }


    suspend fun getDetail(id: String): ListStoryItem {
        return apiService.getDetailStory(id).listStory
    }

    suspend fun clearSession() {
        userPreference.logout()
    }

    fun register(name: String, email: String, password: String) = liveData {
        emit(StateResult.InProgress)
        try {
            val successResponse = apiService.register(name, email, password)
            emit(StateResult.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
            emit(errorResponse.message?.let { StateResult.Failure(it) })
        }
    }

    fun login(email: String, password: String) = liveData {
        emit(StateResult.InProgress)
        try {
            val successResponse = apiService.login(email, password)
            emit(StateResult.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            emit(errorResponse.message.let { StateResult.Failure(it) })
        }
    }


    fun uploadStory(imageFile: File, description: String) = liveData {
        emit(StateResult.InProgress)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo", imageFile.name, requestImageFile
        )
        try {
            val successResponse = apiService.uploadStory(multipartBody, requestBody)
            emit(StateResult.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, StoryResponse::class.java)
            emit(errorResponse?.message?.let { StateResult.Failure(it) })
        }

    }

    suspend fun getStoriesWithLocation(): StoryResponse {
        return apiService.getStoriesWithLocation()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(apiService: ApiService, userPreference: UserPreference, storyDatabase: StoryDatabase) =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userPreference, storyDatabase)
            }.also { instance = it }
    }
}