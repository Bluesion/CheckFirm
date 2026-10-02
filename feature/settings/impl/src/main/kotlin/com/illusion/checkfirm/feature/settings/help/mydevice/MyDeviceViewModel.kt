package com.illusion.checkfirm.feature.settings.help.mydevice

import android.app.Application
import android.os.Build
import android.provider.Settings
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

@HiltViewModel
class MyDeviceViewModel @Inject constructor(application: Application) : ViewModel() {
    val uiState = MutableStateFlow(MyDeviceUiState(
        userName = Settings.Global.getString(application.contentResolver, Settings.Global.DEVICE_NAME).orEmpty(),
        deviceName = Settings.Global.getString(application.contentResolver, "default_device_name").orEmpty(),
        model = Build.MODEL.orEmpty(),
        csc = runCatching {
            Runtime.getRuntime().exec(arrayOf("/system/bin/getprop", "ro.csc.sales_code"))
                .inputStream.bufferedReader().use { it.readLine().orEmpty().uppercase(Locale.US) }
        }.getOrDefault(""),
    )).asStateFlow()
}
