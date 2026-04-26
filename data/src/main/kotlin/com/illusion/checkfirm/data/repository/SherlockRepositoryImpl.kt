package com.illusion.checkfirm.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.illusion.checkfirm.domain.model.SearchResult
import com.illusion.checkfirm.domain.repository.SherlockRepository
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SherlockRepositoryImpl @Inject constructor() : SherlockRepository {
    private val db = FirebaseFirestore.getInstance()

    override suspend fun addToFireStore(
        searchResult: SearchResult,
        userInput: String,
        isBetaFirmware: Boolean,
        profileName: String
    ) {
        val model = searchResult.device.model
        val csc = searchResult.device.csc
        val docRef = db.collection(model).document(csc)
        val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date())

        if (searchResult.firmware.testFirmware.latestFirmware.isBlank()) {
            if (searchResult.firmware.testFirmware.clue == "null") {
                docRef.update(
                    mapOf(
                        "watson" to profileName,
                        "clue" to userInput,
                        "date_latest" to currentDate
                    )
                ).await()
            } else {
                if (searchResult.firmware.testFirmware.clue != userInput && !isBetaFirmware) {
                    // Assuming proper validation happened before calling this repository
                    docRef.update(
                        mapOf(
                            "watson" to profileName,
                            "clue" to userInput,
                            "date_latest" to currentDate
                        )
                    ).await()
                }
            }
        } else {
            docRef.update(
                mapOf(
                    "watson" to profileName,
                    "firmware_decrypted" to userInput,
                    "date_latest" to currentDate
                )
            ).await()
        }
    }
}
