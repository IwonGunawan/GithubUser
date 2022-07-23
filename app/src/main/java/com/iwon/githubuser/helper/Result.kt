package com.iwon.githubuser.helper


sealed class Result<out R> private constructor(){

    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val error : Throwable) : Result<Nothing>()
    object Loading : Result<Nothing>()
}
