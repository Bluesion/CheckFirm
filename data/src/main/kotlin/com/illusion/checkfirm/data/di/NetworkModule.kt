package com.illusion.checkfirm.data.di

import com.illusion.checkfirm.data.mapper.FirmwareXmlConverter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import jakarta.inject.Singleton
import kotlinx.serialization.json.Json
import nl.adaptivity.xmlutil.serialization.XML

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(engineFactory = Android)
    }

    @Provides
    @Singleton
    fun provideJsonClient(client: HttpClient): HttpClient {
        return client.config {
            install(plugin = ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    }
                )
            }
        }
    }

    @Provides
    @Singleton
    fun provideXmlClient(client: HttpClient): HttpClient {
        return client.config {
            install(plugin = ContentNegotiation) {
                register(
                    contentType = ContentType.Application.Xml,
                    converter = FirmwareXmlConverter(XML())
                )
            }
        }
    }
}
