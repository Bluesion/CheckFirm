package com.illusion.checkfirm.core.domain.remote

interface SherlockDataSource {
    suspend fun update(model: String, csc: String, fields: Map<String, Any>)
}
