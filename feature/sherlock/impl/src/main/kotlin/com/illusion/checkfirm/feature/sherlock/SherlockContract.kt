package com.illusion.checkfirm.feature.sherlock

import com.illusion.checkfirm.feature.sherlock.util.SherlockStatus

data class SherlockUiState(
    val selectedTab: Int = 0,

    // Manual tab — six-char body fields.
    val manualBuild: String = "",
    val manualCsc: String = "",
    val manualBaseband: String = "",

    // Immutable model-derived prefixes (XML calls these "buildPrefix" etc.).
    val buildPrefix: String = "",
    val cscPrefix: String = "",
    val basebandPrefix: String = "",

    // Script tab range (inclusive). Both six chars when valid.
    val scriptStart: String = "",
    val scriptEnd: String = "",

    // Last decrypted firmware (success) or assembled user input (otherwise).
    val decryptedFirmware: String = "",

    val status: SherlockStatus = SherlockStatus.INITIAL,
    val showInfoDialog: Boolean = false,
)
