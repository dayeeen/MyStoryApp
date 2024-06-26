package com.dayeeen.mystoryapp.data.retrofit

import com.dayeeen.mystoryapp.data.response.DetailStoryResponse
import com.dayeeen.mystoryapp.data.response.LoginResponse
import com.dayeeen.mystoryapp.data.response.RegisterResponse
import com.dayeeen.mystoryapp.data.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

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

}
