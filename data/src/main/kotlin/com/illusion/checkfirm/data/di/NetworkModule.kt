package com.illusion.checkfirm.data.di

import com.google.firebase.firestore.FirebaseFirestore
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
import jakarta.inject.Qualifier
import jakarta.inject.Singleton
import kotlinx.serialization.json.Json
import nl.adaptivity.xmlutil.serialization.XML

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class JsonClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class XmlClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CommonClient

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    @CommonClient
    fun provideHttpClient(): HttpClient {
        return HttpClient(engineFactory = Android)
    }

    @Provides
    @Singleton
    @JsonClient
    fun provideJsonClient(@CommonClient client: HttpClient): HttpClient {
        return client.config {
            install(plugin = ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    },
                )
            }
        }
    }

    @Provides
    @Singleton
    @XmlClient
    fun provideXmlClient(@CommonClient client: HttpClient): HttpClient {
        return client.config {
            install(plugin = ContentNegotiation) {
                register(
                    contentType = ContentType.Application.Xml,
                    converter = FirmwareXmlConverter(XML()),
                )
            }
        }
    }
}
