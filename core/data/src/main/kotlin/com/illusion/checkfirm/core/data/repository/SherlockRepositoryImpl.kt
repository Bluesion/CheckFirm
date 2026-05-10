package com.illusion.checkfirm.core.data.repository

import com.illusion.checkfirm.core.domain.model.SearchResult
import com.illusion.checkfirm.core.domain.remote.SherlockDataSource
import com.illusion.checkfirm.core.domain.repository.SherlockRepository
import jakarta.inject.Inject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SherlockRepositoryImpl @Inject constructor(
    private val sherlockDataSource: SherlockDataSource,
) : SherlockRepository {

    override suspend fun addToFireStore(
        searchResult: SearchResult,
        userInput: String,
        isBetaFirmware: Boolean,
        profileName: String
    ) {
        val model = searchResult.device.model
        val csc = searchResult.device.csc
        val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date())
        val testFirmware = searchResult.firmware.testFirmware

        if (testFirmware.latestFirmware.isBlank()) {
            if (testFirmware.clue == "null") {
                sherlockDataSource.update(
                    model = model,
                    csc = csc,
                    fields = mapOf(
                        "watson" to profileName,
                        "clue" to userInput,
                        "date_latest" to currentDate
                    )
                )
            } else if (testFirmware.clue != userInput && !isBetaFirmware) {
                // Assuming proper validation happened before calling this repository
                sherlockDataSource.update(
                    model = model,
                    csc = csc,
                    fields = mapOf(
                        "watson" to profileName,
                        "clue" to userInput,
                        "date_latest" to currentDate
                    )
                )
            }
        } else {
            sherlockDataSource.update(
                model = model,
                csc = csc,
                fields = mapOf(
                    "watson" to profileName,
                    "firmware_decrypted" to userInput,
                    "date_latest" to currentDate
                )
            )
        }
    }
}
