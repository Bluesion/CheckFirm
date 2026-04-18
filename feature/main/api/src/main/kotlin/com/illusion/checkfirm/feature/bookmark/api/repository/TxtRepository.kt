package com.deepmedi.android.sdk.superapp.feature.txtviewer.api.repository

import com.deepmedi.android.sdk.superapp.feature.txtviewer.api.model.RgbTxtFile

interface TxtRepository {
    suspend fun getTxtFileList(): List<RgbTxtFile>
}
