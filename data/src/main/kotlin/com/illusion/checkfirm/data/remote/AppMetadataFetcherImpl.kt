package com.illusion.checkfirm.data.remote

import com.illusion.checkfirm.domain.model.ApiResponse
import com.illusion.checkfirm.domain.model.AppVersionStatus
import com.illusion.checkfirm.domain.remote.AppMetadataFetcher
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
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

        /*
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    CheckFirmLogger.d(message)
                }
            }
            level = LogLevel.ALL
        }
        */

        expectSuccess = false
        HttpResponseValidator {
            validateResponse { response ->
                if (!response.status.isSuccess()) {
                    throw ApiException(response, response.bodyAsText())
                }
            }
            handleResponseExceptionWithRequest { exception, request ->
                val clientException =
                    exception as? ApiException ?: return@handleResponseExceptionWithRequest
                throw clientException
            }
        }
    }

    override suspend fun checkAppVersion(currentAppVersion: Int): ApiResponse<AppVersionStatus> {
        return try {
            val response =
                client.get("https://api.github.com/repos/Bluesion/CheckFirm/contents/metadata.json") {
                    headers {
                        append("Accept", "application/vnd.github.raw+json")
                    }
                }

            val appVersionData = response.body<AppMetadataInfo>().appVersionData

            if (currentAppVersion < appVersionData.minimum.versionCode) {
                ApiResponse.Success(AppVersionStatus.UPDATE_REQUIRED)
            } else if (currentAppVersion < appVersionData.latest.versionCode) {
                ApiResponse.Success(AppVersionStatus.OLD_VERSION)
            } else {
                ApiResponse.Success(AppVersionStatus.LATEST_VERSION)
            }
        } catch (_: UnknownHostException) {
            ApiResponse.Error.NetworkError
        } catch (_: ApiException) {
            ApiResponse.Error.ServerError
        } catch (_: Exception) {
            ApiResponse.Error.UnknownError
        }
    }
}