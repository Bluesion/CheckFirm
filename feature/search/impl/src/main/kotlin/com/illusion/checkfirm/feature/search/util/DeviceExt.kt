package com.illusion.checkfirm.feature.search.util

import com.illusion.checkfirm.core.domain.model.Device

fun Device.isValid(): Boolean {
    return isValidModel(this.model) && isValidCSC(this.csc)
}

private fun isValidModel(model: String): Boolean {
    if (model.count { it == '-' } != 1) return false

    val parts = model.split("-")
    if (parts.size != 2) return false

    val prefix = parts[0]
    val code = parts[1]

    if (prefix.length !in 2..3) return false
    if (!prefix.all { it in 'A'..'Z' }) return false
    if (prefix.firstOrNull() != 'S') return false

    if (code.length !in 3..12) return false
    if (!code.all { it in 'A'..'Z' || it in '0'..'9' }) return false

    return true
}

private fun isValidCSC(csc: String): Boolean {
    if (csc.length != 3) return false
    return csc.all { it in 'A'..'Z' || it in '0'..'9' }
}
