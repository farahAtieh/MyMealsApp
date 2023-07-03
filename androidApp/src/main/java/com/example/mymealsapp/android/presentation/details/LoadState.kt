package com.example.mymealsapp.android.presentation.details

interface ErrorGettable {

    fun getErrorIfExist(): Throwable?
}

sealed class LoadState<out T> : ErrorGettable {
    object Loading : LoadState<Nothing>()
    class Loaded<T>(val value: T) : LoadState<T>()
    class Error<T>(val e: Throwable) : LoadState<T>()

    val isLoading get() = this is Loading
    override fun getErrorIfExist(): Throwable? = if (this is Error) e else null
    fun getValueOrNull(): T? = if (this is Loaded<T>) value else null
}

