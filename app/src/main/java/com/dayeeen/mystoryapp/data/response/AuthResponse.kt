package com.dayeeen.mystoryapp.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class LoginResponse(
	@field:SerializedName("loginResult")
	var loginResult: LoginResult,

	@field:SerializedName("error")
	var error: Boolean,

	@field:SerializedName("message")
	var message: String
)

data class LoginResult(
	@field:SerializedName("name")
	var name: String,

	@field:SerializedName("userId")
	var userId: String,

	@field:SerializedName("token")
	var token: String
)

data class RegisterResponse(
	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

@Parcelize
data class StoryDetailResponse (
	var id: String,
	var name: String,
	var description: String,
	var photoUrl: String,
	var createdAt: String,
	var lat: Double,
	var lon: Double
) : Parcelable
