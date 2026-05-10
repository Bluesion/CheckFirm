package com.illusion.checkfirm.core.network

import com.illusion.checkfirm.core.domain.model.ApiResponse
import com.illusion.checkfirm.core.domain.model.AppMetadata
import com.illusion.checkfirm.core.domain.remote.AppMetadataFetcher
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.serialization.kotlinx.json.json
import jakarta.inject.Inject
import kotlinx.serialization.json.Json
import java.net.UnknownHostException

class AppMetadataFetcherImpl @Inject constructor() : AppMetadataFetcher {

    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                }
            )
        }
    }

    override suspend fun fetchAppMetadata(): ApiResponse<AppMetadata> {
        return try {
            client.get(urlString = "https://api.github.com/repos/Bluesion/CheckFirm/contents/metadata.json") {
                headers {
                    append(name = "Accept", value = "application/vnd.github.raw+json")
                }
            }.body()
        } catch (_: UnknownHostException) {
            ApiResponse.Error.NetworkError
        } catch (_: ApiException) {
            ApiResponse.Error.ServerError
        } catch (_: Exception) {
            ApiResponse.Error.UnknownError
        }
    }
}