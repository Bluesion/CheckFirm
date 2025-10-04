package com.illusion.checkfirm.common.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.DisplayMetrics
import com.illusion.checkfirm.R
import com.illusion.checkfirm.data.model.local.DeviceItem
import java.io.BufferedReader
import java.io.InputStreamReader
import java.math.BigInteger
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object Tools {
    fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val n = cm.activeNetwork
        val nc = cm.getNetworkCapabilities(n)
        return if (n != null) {
            nc?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) == true
        } else {
            false
        }
    }

    fun dateToString(
        date: Date,
        format: String = "yyyy-MM-dd",
        locale: Locale = Locale.KOREAN
    ): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(date)
    }

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

    /**
     * 펌웨어 정보 중 빌드 정보만 추출하는 함수
     * A720SKSU5CUJ2/A720SSKC5CUJ2/A720NKOU5CUJ1 -> A720SKSU5CUJ2
     *
     * 비정상적인 펌웨어 정보가 들어오면 빈 문자열을 반환한다.
     */
    fun getFullBuildInfo(firmware: String): String {
        if (firmware.isBlank()) {
            return ""
        }

        val slashIndex = firmware.indexOf("/")
        if (slashIndex == -1) {
            return firmware
        }

        return firmware.split("/")[0]
    }

    /**
     * 펌웨어 정보를 6자리로 만들어주는 함수
     * A720SKSU5CUJ2/A720SSKC5CUJ2/A720NKOU5CUJ1 -> U5CUJ2
     *
     * 비정상적인 펌웨어 정보가 들어오면 빈 문자열을 반환한다.
     */
    fun getBuildInfo(firmware: String): String {
        val temp = getFullBuildInfo(firmware)

        if (temp.isEmpty()) {
            return ""
        }

        val underScoreIndex = temp.indexOf("_")
        val dotIndex = temp.indexOf(".")

        return if (underScoreIndex > dotIndex) {
            temp.substring(dotIndex - 6, dotIndex)
        } else if (underScoreIndex == dotIndex) {
            temp.substring(temp.length - 6)
        } else {
            temp.substring(underScoreIndex - 6, underScoreIndex)
        }
    }

    /**
     * 펌웨어 정보를 4자리로 만들어주는 함수
     * A720SKSU5CUJ2/A720SSKC5CUJ2/A720NKOU5CUJ1 -> CUJ2
     *
     * 비정상적인 펌웨어 정보가 들어오면 빈 문자열을 반환한다.
     */
    fun getShortBuildInfo(firmware: String): String {
        val temp = getBuildInfo(firmware)

        if (temp.isEmpty()) {
            return ""
        }

        return temp.substring(2)
    }

    fun isBetaFirmware(firmware: String): Boolean {
        return getShortBuildInfo(firmware)[0] == 'Z'
    }

    fun getMD5Hash(text: String): String {
        val md = MessageDigest.getInstance("MD5")
        val bigInt = BigInteger(1, md.digest(text.toByteArray(Charsets.UTF_8)))
        return String.format("%032x", bigInt)
    }

    private fun isValidModel(model: String): Boolean {
        // Hyphen이 하나만 있는지 확인
        if (model.count { it == '-' } != 1) {
            return false
        }

        // SM-A720S에서 SM이 prefix, A720S는 code
        val prefix = model.split("-")[0]
        val code = model.split("-")[1]

        // SM, SC, SCG, SCV 등 2-3글자 prefix만 지원
        if (prefix.length !in 2..3) {
            return false
        }

        // prefix가 영어 대문자만으로 구성되어 있는지 확인
        prefix.forEach {
            if (it.code !in 65..90) {
                return false
            }
        }

        // prefix가 S로 시작하는지 확인 (GT로 시작하는 구형 모델 미지원)
        if (prefix[0] != 'S') {
            return false
        }

        // 가장 짧은 건 code가 3자리 (SC-03L 등 라우터)
        // SM-A025VZKAVZW 같은 특수 케이스 존재
        // 위 보다 긴 건 못 봤는데, 혹시 몰라서 12자까지 제한
        if (code.length !in 3..12) {
            return false
        }

        // 코드가 영어 대문자나 숫자가 아닌 경우 걸러내기
        for (ch in code) {
            if (ch.code !in 65..90 && ch.code !in 48..57) {
                return false
            }
        }

        return true
    }

    private fun isValidCSC(csc: String): Boolean {
        if (csc.length != 3) {
            return false
        }

        // 코드가 영어 대문자나 숫자가 아닌 경우 걸러내기
        for (ch in csc) {
            if (ch.code !in 65..90 && ch.code !in 48..57) {
                return false
            }
        }

        return true
    }

    fun isValidDevice(deviceItem: DeviceItem): Boolean {
        return isValidModel(deviceItem.model) && isValidCSC(deviceItem.csc)
    }

    fun isValidDevice(model: String, csc: String): Boolean {
        return isValidModel(model) && isValidCSC(csc)
    }

    fun isCorrectBuildNumber(buildNumber: String): Boolean {
        return buildNumber.length == 6
    }

    fun compareBuildNumber(original: String, new: String): Int {
        when {
            original[0] > new[0] -> {
                return 1
            }

            original[0] == new[0] -> {
                when {
                    original[1] > new[1] -> {
                        return 2
                    }

                    original[1] == new[1] -> {
                        when {
                            original[2] > new[2] -> {
                                return 3
                            }

                            original[2] == new[2] -> {
                                when {
                                    original[3] > new[3] -> {
                                        return 4
                                    }

                                    original[3] == new[3] -> {
                                        when {
                                            original[4] > new[4] -> {
                                                return 5
                                            }

                                            original[4] == new[4] -> {
                                                return when {
                                                    original[5] > new[5] -> {
                                                        6
                                                    }

                                                    original[5] == new[5] -> {
                                                        -1
                                                    }

                                                    else -> {
                                                        0
                                                    }
                                                }
                                            }

                                            else -> {
                                                return 0
                                            }
                                        }
                                    }

                                    else -> {
                                        return 0
                                    }
                                }
                            }

                            else -> {
                                return 0
                            }
                        }
                    }

                    else -> {
                        return 0
                    }
                }
            }

            else -> {
                return 0
            }
        }
    }

    fun compareFirmware(original: String, new: String): Int {
        val originalBuildNumber = getBuildInfo(original)
        val newBuildNumber = getBuildInfo(new)

        return compareBuildNumber(originalBuildNumber, newBuildNumber)
    }

    fun isEncrypted(character: Char): Boolean {
        return Character.isLowerCase(character) || Character.isDigit(character)
    }

    fun getCSC(): String {
        val process = Runtime.getRuntime().exec(
            arrayOf("/system/bin/getprop", "ro.csc.sales_code")
        )
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        val csc = reader.readLine()
        reader.close()

        return if (csc.isNullOrBlank()) {
            ""
        } else {
            csc.uppercase(Locale.US)
        }
    }

    fun isValidUserName(name: String): Boolean {
        return name.length < 11
    }

    fun getDeviceType(model: String): Int {
        val dashIndex = model.indexOf("-")

        return when (model[dashIndex + 1]) {
            'T' -> com.bluesion.oneui.R.drawable.oneui_ic_tablet
            'R' -> {
                if (model[dashIndex + 2] >= '8') {
                    com.bluesion.oneui.R.drawable.oneui_ic_watch
                } else if (model[dashIndex + 2] == '5') {
                    if (model[dashIndex + 3] == '3') {
                        com.bluesion.oneui.R.drawable.oneui_ic_buds3
                    } else {
                        com.bluesion.oneui.R.drawable.oneui_ic_watch
                    }
                }
                if (model[dashIndex + 2] == '5' && model[dashIndex + 3] == '3') {
                    com.bluesion.oneui.R.drawable.oneui_ic_buds3
                } else if (model[dashIndex + 2] == '6') {
                    com.bluesion.oneui.R.drawable.oneui_ic_buds3
                } else {
                    com.bluesion.oneui.R.drawable.oneui_ic_buds
                }
            }

            'Q' -> com.bluesion.oneui.R.drawable.oneui_ic_ring
            'L' -> com.bluesion.oneui.R.drawable.oneui_ic_watch
            else -> com.bluesion.oneui.R.drawable.oneui_ic_phone
        }
    }

    fun dpToPx(context: Context, dp: Float): Int {
        return (dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
    }

    fun pxToDp(context: Context, px: Float): Int {
        return (px / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
    }

    fun getCategory(context: Context, category: String?): String {
        return if (category.isNullOrBlank() || category == context.getString(R.string.category_all)) {
            ""
        } else {
            category
        }
    }
}
