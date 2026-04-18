package com.deepmedi.android.sdk.superapp.feature.txtviewer.api.usecase

import com.deepmedi.android.sdk.superapp.feature.txtviewer.api.model.RgbTxtFile

interface GetTxtUseCase {
    suspend operator fun invoke(): List<RgbTxtFile>
}
