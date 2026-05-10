package com.illusion.checkfirm.core.domain.repository

import com.illusion.checkfirm.core.domain.model.SearchResult

interface SherlockRepository {
    suspend fun addToFireStore(
        searchResult: SearchResult,
        userInput: String,
        isBetaFirmware: Boolean,
        profileName: String
    )
}
