package com.illusion.checkfirm.data.model.remote

sealed class ApiResponse<out T> {
    data class Success<out T>(val data: T) : ApiResponse<T>()
    sealed class Error<out T> : ApiResponse<T>() {
        object NetworkError : Error<Nothing>()
        object ServerError : Error<Nothing>()
        object UnknownError : Error<Nothing>()
        data class ApiError(val httpStatus: Int, val message: String) : Error<Nothing>()
    }
    object Loading : ApiResponse<Nothing>()
}