package com.illusion.checkfirm.feature.sherlock.util

import java.math.BigInteger
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Helpers for Sherlock's firmware-decryption algorithm. Ported from
 * `common/util/Tools.kt` in the legacy XML app.
 *
 * The encrypted firmware hashes Samsung publishes are MD5 over the full build
 * string `"<buildPrefix><build>/<cscPrefix><csc>/<basebandPrefix><baseband>"`,
 * UTF-8 encoded, hex-formatted (lowercase). Sherlock reverses this by either
 * accepting a manual full string or sweeping a script range and hashing each
 * permutation until one matches.
 */
internal object SherlockTools {

    fun md5Hash(text: String): String {
        val md = MessageDigest.getInstance("MD5")
        val bigInt = BigInteger(1, md.digest(text.toByteArray(Charsets.UTF_8)))
        return String.format("%032x", bigInt)
    }

    /** A correct build number is exactly 6 chars. */
    fun isCorrectBuildNumber(buildNumber: String): Boolean = buildNumber.length == 6

    /**
     * Returns 0 if [new] > [original]. Returns -1 if equal. Otherwise returns the
     * 1-based index of the first character where original > new (1=bootloader,
     * 2=oneUI, 3=year, 4=month, 5=revision … wait, actually the legacy compare
     * goes char-by-char from index 0; the call site uses the result to pick a
     * warning). Translated verbatim from legacy Tools.kt.
     */
    fun compareBuildNumber(original: String, new: String): Int {
        if (original[0] > new[0]) return 1
        if (original[0] != new[0]) return 0
        if (original[1] > new[1]) return 2
        if (original[1] != new[1]) return 0
        if (original[2] > new[2]) return 3
        if (original[2] != new[2]) return 0
        if (original[3] > new[3]) return 4
        if (original[3] != new[3]) return 0
        if (original[4] > new[4]) return 5
        if (original[4] != new[4]) return 0
        if (original[5] > new[5]) return 6
        if (original[5] == new[5]) return -1
        return 0
    }

    /**
     * Build the year+month suffix for today using the same encoding Samsung uses
     * in its firmware version strings. Year is 'K' for 2011 (offset 75 + Y-2011),
     * month is 'A' for January (offset 65 + M-1).
     */
    fun todayYearMonth(): String {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH) + 1
        val y = (75 + (year - 2011)).toChar()
        val m = (65 + (month - 1)).toChar()
        return "$y$m"
    }

    fun isBetaFirmware(firmware: String): Boolean {
        val build = fullBuild(firmware).ifEmpty { return false }
        return build.length >= 6 && build[build.length - 6 + 2] == 'Z'
    }

    /** Returns the build component (before the first '/') or empty if invalid. */
    fun fullBuild(firmware: String): String {
        if (firmware.isBlank()) return ""
        val slash = firmware.indexOf('/')
        return if (slash == -1) firmware else firmware.substring(0, slash)
    }

    fun dateString(date: Date = Calendar.getInstance().time): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN).format(date)
}
