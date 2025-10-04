package com.illusion.checkfirm.data.model.remote

import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.HttpResponse

class ApiException(response: HttpResponse, cachedResponseText: String) :
    ResponseException(response, cachedResponseText) {

    val code: Int = response.status.value
    override val message: String =
        "Got $code error from ${response.call.request.url}. Response body: $cachedResponseText"
}