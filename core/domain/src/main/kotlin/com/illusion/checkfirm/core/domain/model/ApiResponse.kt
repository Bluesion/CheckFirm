package com.illusion.checkfirm.core.domain.model

sealed class ApiResponse<out T> {
    data class Success<out T>(val data: T) : ApiResponse<T>()
    sealed class Error : ApiResponse<Nothing>() {
        data object NetworkError : Error()
        data object ServerError : Error()
        data object UnknownError : Error()
    }
}
