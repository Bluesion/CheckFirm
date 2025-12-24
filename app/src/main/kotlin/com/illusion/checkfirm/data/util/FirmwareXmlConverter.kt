package com.illusion.checkfirm.data.util

import com.illusion.checkfirm.data.model.remote.FirmwareVersionInfo
import io.ktor.http.ContentType
import io.ktor.http.content.OutgoingContent
import io.ktor.serialization.ContentConverter
import io.ktor.util.reflect.TypeInfo
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.charsets.Charset
import io.ktor.utils.io.readRemaining
import kotlinx.io.readByteArray
import nl.adaptivity.xmlutil.serialization.XML

class FirmwareXmlConverter(private val format: XML) : ContentConverter {

    override suspend fun serialize(
        contentType: ContentType,
        charset: Charset,
        typeInfo: TypeInfo,
        value: Any?
    ): OutgoingContent {
        throw UnsupportedOperationException("Serialization not supported")
    }

    override suspend fun deserialize(
        charset: Charset,
        typeInfo: TypeInfo,
        content: ByteReadChannel
    ): FirmwareVersionInfo? {
        val bytes = content.readRemaining().readByteArray()
        val xmlString = String(bytes)

        return if (xmlString.contains("<Error>")) {
            null
        } else {
            format.decodeFromString(FirmwareVersionInfo.serializer(), xmlString)
        }
    }
}