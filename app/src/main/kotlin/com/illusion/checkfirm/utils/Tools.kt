package com.illusion.checkfirm.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import org.spongycastle.crypto.digests.MD5Digest
import org.spongycastle.util.encoders.Hex
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*

object Tools {
    fun isOnline(mContext: Context): Boolean {
        val cm = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val n = cm.activeNetwork
        val nc = cm.getNetworkCapabilities(n)
        return if (n != null) {
            (nc!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) || (nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
        } else {
            false
        }
    }

    fun dateToString(date: Date, format: String = "yyyy/MM/dd", locale: Locale = Locale.KOREAN): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(date)
    }

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

    fun getFirmwareInfo(firmware: String): String {
        return if (firmware.length < 6) {
            "??????"
        } else {
            val underBar = firmware.contains("_")
            val dot = firmware.contains(".")
            val slash = firmware.contains("/")

            if ((underBar && dot) || (underBar && !dot)) {
                val index = firmware.indexOf("_")
                firmware.substring(index - 6, index)
            } else if (!underBar && dot) {
                val index = firmware.indexOf(".")
                firmware.substring(index - 6, index)
            } else if (!underBar && !dot) {
                if (slash) {
                    val index = firmware.indexOf("/")
                    firmware.substring(index - 6, index)
                } else {
                    "??????"
                }
            } else {
                "??????"
            }
        }
    }

    fun getMD5Hash(text: String): String? {
        var newText = text.toByteArray(StandardCharsets.UTF_8)
        val localMD5Digest = MD5Digest()
        localMD5Digest.update(newText, 0, newText.size)
        newText = ByteArray(localMD5Digest.digestSize)
        localMD5Digest.doFinal(newText, 0)
        return Hex.toHexString(newText)
    }
}
