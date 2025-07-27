package com.illusion.checkfirm.features.search.util

enum class SearchValidationResult(val code: Int) {
    SUCCESS(0),
    DUPLICATED_DEVICE(-1),
    INVALID_DEVICE(-2),
    MAX_SEARCH_CAPACITY_EXCEEDED(-3)
}