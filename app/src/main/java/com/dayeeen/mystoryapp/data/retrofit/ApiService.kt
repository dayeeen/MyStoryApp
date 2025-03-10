package com.dayeeen.mystoryapp.data.retrofit

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dayeeen.mystoryapp.data.response.DetailStoryResponse
import com.dayeeen.mystoryapp.data.response.LoginResponse
import com.dayeeen.mystoryapp.data.response.RegisterResponse
import com.dayeeen.mystoryapp.data.response.StoryResponse
import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // Register
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse


    // Login
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String, @Field("password") password: String
    ): LoginResponse


    // Get Stories
    @GET("stories")
    suspend fun getStories(): StoryResponse

    // Get Detail Story
    @GET("stories/{id}")
    suspend fun getDetailStory(
        @Path("id") id: String
    ): DetailStoryResponse


    // Upload Story
    @Multipart
    @POST("stories")
    suspend fun uploadStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): StoryResponse

    // Get Stories Location
    @GET("stories")
    suspend fun getStoriesWithLocation(
        @Query("location") location : Int = 1,
    ): StoryResponse

    // Get Stories with Paging 3
    @GET("stories")
    suspend fun getStories(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20
    ): StoryResponse

}
