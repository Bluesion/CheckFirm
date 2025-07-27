package com.illusion.checkfirm.data.source.remote

import com.illusion.checkfirm.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class MainDataSource {

    suspend fun checkAppVersion(): Boolean = withContext(Dispatchers.IO) {
        var isOldVersion = true

        try {
            val doc =
                Jsoup.connect("https://raw.githubusercontent.com/Bluesion/CheckFirm/master/VERSION")
                    .timeout(10000)
                    .get()
            val body = doc.body().text()
            val index = body.lastIndexOf("$") + 1
            val data = Integer.parseInt(body.substring(index))
            isOldVersion = BuildConfig.VERSION_CODE < data
        } catch (_: Exception) {
        } catch (_: Error) {
        }

        return@withContext isOldVersion
    }
}