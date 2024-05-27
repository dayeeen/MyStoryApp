package com.dayeeen.mystoryapp.utils

sealed class StateResult<out T> private constructor() {
    data class Success<out U>(val data: U) : StateResult<U>()
    data class Failure(val message: String) : StateResult<Nothing>()
    data object InProgress : StateResult<Nothing>()
}
