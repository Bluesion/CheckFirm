package com.illusion.checkfirm.feature.forceupdate

import android.content.Intent
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri

@Composable
fun ForceUpdateRoute() {
    val activity = LocalActivity.current
    val context = LocalContext.current

    ForceUpdateScreen(
        onUpdateButtonClick = {
            context.startActivity(
                Intent(Intent.ACTION_VIEW).apply {
                    data = ("market://details?id=" + context.applicationContext.packageName).toUri()
                }
            )
        },
        onCloseButtonClick = {
            activity?.finish()
        },
    )
}
