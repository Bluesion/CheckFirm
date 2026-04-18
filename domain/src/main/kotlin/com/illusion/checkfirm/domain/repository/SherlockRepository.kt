package com.illusion.checkfirm.domain.repository

import com.illusion.checkfirm.domain.model.SearchResult

interface SherlockRepository {
    suspend fun addToFireStore(
        searchResult: SearchResult,
        userInput: String,
        isBetaFirmware: Boolean,
        profileName: String
    )
}
