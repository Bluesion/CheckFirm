package com.illusion.checkfirm.core.network

import com.google.firebase.firestore.FirebaseFirestore
import com.illusion.checkfirm.core.domain.remote.SherlockDataSource
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await

class SherlockDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
) : SherlockDataSource {

    override suspend fun update(model: String, csc: String, fields: Map<String, Any>) {
        firestore.collection(model).document(csc).update(fields).await()
    }
}
