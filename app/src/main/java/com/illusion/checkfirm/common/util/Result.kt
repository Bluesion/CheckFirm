package com.illusion.checkfirm.common.util

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val errorCode: Int, val message: String = "", val throwable: Throwable? = null) : Result<Nothing>()
    object Loading : Result<Nothing>() // Use 'object' for singleton states

    inline fun <R> map(transform: (T) -> R): Result<R> {
        return when (this) {
            is Success -> Success(transform(data))
            is Error -> Error(errorCode, message, throwable)
            Loading -> Loading
        }
    }

    inline fun onSuccess(action: (T) -> Unit): Result<T> {
        if (this is Success) {
            action(data)
        }
        return this
    }

    inline fun onError(action: (Int, String, Throwable?) -> Unit): Result<T> {
        if (this is Error) {
            action(errorCode, message, throwable)
        }
        return this
    }

    inline fun onLoading(action: () -> Unit): Result<T> {
        if (this is Loading) {
            action()
        }
        return this
    }
}