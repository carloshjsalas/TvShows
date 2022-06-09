package com.cs.tvshows.utils

sealed class Outcome<T>(
    val data: T? = null,
    val error: Throwable? = null
) {
    class Success<T>(data: T) : Outcome<T>(data)
    class Loading<T>(data: T? = null) : Outcome<T>(data)
    class Error<T>(throwable: Throwable? = null, data: T? = null) : Outcome<T>(data, throwable)
}
