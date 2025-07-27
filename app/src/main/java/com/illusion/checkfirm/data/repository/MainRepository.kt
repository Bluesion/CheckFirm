package com.illusion.checkfirm.data.repository

import android.content.Context
import com.illusion.checkfirm.common.util.Tools
import com.illusion.checkfirm.data.source.remote.MainDataSource

class MainRepository(private val mainDataSource: MainDataSource, private val context: Context) {

    suspend fun checkAppVersion(): Boolean {
        return if (Tools.isOnline(context)) {
            mainDataSource.checkAppVersion()
        } else {
            return false
        }
    }
}