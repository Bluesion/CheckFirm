package com.illusion.checkfirm.feature.sherlock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.illusion.checkfirm.core.domain.model.SearchResult
import com.illusion.checkfirm.core.preference.api.PreferenceRepository
import com.illusion.checkfirm.feature.sherlock.util.SherlockStatus
import com.illusion.checkfirm.feature.sherlock.util.SherlockTools
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * Ports the legacy SherlockViewModel.
 *
 * High-level: Samsung publishes the "test" firmware version as an MD5 hash of the
 * full build string. Sherlock reverses this two ways — manual entry where the
 * user types the build/csc/baseband bodies and we hash on every keystroke, or a
 * script range where we sweep all valid 6-char permutations.
 *
 * Side effects:
 * - Writes the decrypted version back to Firestore (only when the user has
 *   firebase analytics enabled, matching the legacy gating).
 */
@HiltViewModel
class SherlockViewModel @Inject constructor(
    private val preferenceRepository: PreferenceRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SherlockUiState())
    val uiState: StateFlow<SherlockUiState> = _uiState.asStateFlow()

    private var searchResult: SearchResult? = null

    private val firestore by lazy { FirebaseFirestore.getInstance() }

    fun initialize(payload: SearchResult?) {
        if (payload == null) {
            _uiState.value = SherlockUiState()
            return
        }
        searchResult = payload

        // Pick the best reference firmware for prefix derivation:
        // prefer the test "clue" if present (a previously-decrypted hint),
        // otherwise the official latest.
        val reference = payload.firmware.testFirmware.clue.ifBlank {
            payload.firmware.officialFirmware.latestFirmware
        }

        val firstSlash = reference.indexOf('/')
        if (firstSlash == -1) {
            // No firmware available — fall back to a reasonable empty form.
            // The legacy code used `model.substring(3)` which assumes "SM-XYZ…"
            // and strips the "SM-" prefix to get the device family code.
            val familyCode = payload.device.model.substringAfter('-').take(4)
            val prefix = familyCode + "XX"
            val dummy = "U0A${SherlockTools.todayYearMonth()}1"
            _uiState.value = SherlockUiState(
                buildPrefix = prefix,
                cscPrefix = prefix,
                basebandPrefix = prefix,
                manualBuild = dummy,
                manualCsc = dummy,
                manualBaseband = dummy,
                scriptStart = dummy,
                scriptEnd = "${dummy.substring(0, 3)}${SherlockTools.todayYearMonth()}Z",
                status = SherlockStatus.INITIAL,
            )
            return
        }

        val secondSlash = reference.lastIndexOf('/')
        val build = reference.substring(0, firstSlash)
        val csc = reference.substring(firstSlash + 1, secondSlash)
        val length = build.length

        val buildPrefix = build.substring(0, length - 6)
        val cscPrefix = csc.substring(0, length - 5)
        val manualBuild = build.substring(length - 6)
        val manualCsc = csc.substring(length - 5)

        var basebandPrefix = ""
        var manualBaseband = ""
        if (reference.length - 1 > secondSlash) {
            val baseband = reference.substring(secondSlash + 1)
            basebandPrefix = baseband.substring(0, length - 6)
            manualBaseband = baseband.substring(length - 6)
        }

        _uiState.value = SherlockUiState(
            buildPrefix = buildPrefix,
            cscPrefix = cscPrefix,
            basebandPrefix = basebandPrefix,
            manualBuild = manualBuild,
            manualCsc = manualCsc,
            manualBaseband = manualBaseband,
            scriptStart = manualBuild,
            scriptEnd = "${manualBuild.substring(0, 3)}${SherlockTools.todayYearMonth()}Z",
            status = SherlockStatus.INITIAL,
        )
    }

    fun selectTab(index: Int) =
        _uiState.update { it.copy(selectedTab = index, status = SherlockStatus.INITIAL) }

    fun setBuildPrefix(value: String) {
        _uiState.update { it.copy(buildPrefix = value) }
        compare()
    }

    fun setCscPrefix(value: String) {
        _uiState.update { it.copy(cscPrefix = value) }
        compare()
    }

    fun setBasebandPrefix(value: String) {
        _uiState.update { it.copy(basebandPrefix = value) }
        compare()
    }

    fun setManualBuild(value: String) {
        _uiState.update { it.copy(manualBuild = value) }
        compare()
    }

    fun setManualCsc(value: String) {
        _uiState.update { it.copy(manualCsc = value) }
        compare()
    }

    fun setManualBaseband(value: String) {
        _uiState.update { it.copy(manualBaseband = value) }
        compare()
    }

    fun setScriptStart(value: String) {
        if (!SherlockTools.isCorrectBuildNumber(value)) {
            _uiState.update {
                it.copy(scriptStart = value, status = SherlockStatus.WARNING_SCRIPT_START_INVALID)
            }
            return
        }
        _uiState.update { it.copy(scriptStart = value) }
        validateScript()
    }

    fun setScriptEnd(value: String) {
        if (!SherlockTools.isCorrectBuildNumber(value)) {
            _uiState.update {
                it.copy(scriptEnd = value, status = SherlockStatus.WARNING_SCRIPT_END_INVALID)
            }
            return
        }
        _uiState.update { it.copy(scriptEnd = value) }
        validateScript()
    }

    fun showInfoDialog(show: Boolean) {
        _uiState.update { it.copy(showInfoDialog = show) }
    }

    fun dismissResult() {
        _uiState.update { it.copy(status = SherlockStatus.INITIAL) }
    }

    /**
     * Hash the assembled manual input and compare against the published encrypted
     * firmware. If the test firmware doesn't have a `latestFirmware` we look in
     * the historical map instead.
     */
    private fun compare() {
        val state = _uiState.value
        val assembled =
            "${state.buildPrefix}${state.manualBuild}/${state.cscPrefix}${state.manualCsc}/${state.basebandPrefix}${state.manualBaseband}"

        viewModelScope.launch {
            val md5 = SherlockTools.md5Hash(assembled)
            val test = searchResult?.firmware?.testFirmware ?: return@launch

            val matched = if (test.latestFirmware.isBlank()) {
                test.previousFirmware[md5] != null
            } else {
                test.latestFirmware == md5
            }

            _uiState.update {
                it.copy(
                    decryptedFirmware = assembled,
                    status = if (matched) SherlockStatus.SUCCESS else SherlockStatus.FAIL,
                )
            }
            if (matched) addToFirestore(assembled)
        }
    }

    private fun validateScript() {
        val state = _uiState.value
        if (!SherlockTools.isCorrectBuildNumber(state.scriptStart)) {
            _uiState.update { it.copy(status = SherlockStatus.WARNING_SCRIPT_START_INVALID) }
            return
        }
        if (!SherlockTools.isCorrectBuildNumber(state.scriptEnd)) {
            _uiState.update { it.copy(status = SherlockStatus.WARNING_SCRIPT_END_INVALID) }
            return
        }
        val warning = when (SherlockTools.compareBuildNumber(state.scriptStart, state.scriptEnd)) {
            0, -1 -> SherlockStatus.NO_WARNING
            1, 2 -> SherlockStatus.WARNING_BUILD_NUMBER_BOOTLOADER
            3 -> SherlockStatus.WARNING_BUILD_NUMBER_ONE_UI_VERSION
            4 -> SherlockStatus.WARNING_BUILD_NUMBER_YEAR
            5 -> SherlockStatus.WARNING_BUILD_NUMBER_MONTH
            else -> SherlockStatus.WARNING_BUILD_NUMBER_REVISION
        }
        _uiState.update { it.copy(status = warning) }
    }

    /**
     * Sweep every valid permutation in the script range, hash each, return the
     * first hit. Runs on Default for CPU work; the legacy code did the same.
     */
    fun runScript() {
        val state = _uiState.value
        val test = searchResult?.firmware?.testFirmware ?: return

        _uiState.update { it.copy(status = SherlockStatus.RUNNING) }

        viewModelScope.launch(Dispatchers.Default) {
            val (builds, cscs, basebands) = buildPermutations(state.scriptStart, state.scriptEnd)
            var winner: String? = null

            outer@ for (x in builds.indices) {
                for (y in 0..x) {
                    for (z in 0..x) {
                        val candidate = buildString {
                            append(state.buildPrefix); append(builds[x]); append('/')
                            append(state.cscPrefix); append(cscs[y]); append('/')
                            append(state.basebandPrefix); append(basebands[z])
                        }
                        val md5 = SherlockTools.md5Hash(candidate)
                        val match = if (test.latestFirmware.isBlank()) {
                            test.previousFirmware[md5] != null
                        } else {
                            test.latestFirmware == md5
                        }
                        if (match) {
                            winner = candidate
                            break@outer
                        }
                    }
                }
            }

            withContext(Dispatchers.Main) {
                val found = winner
                if (found == null) {
                    _uiState.update { it.copy(status = SherlockStatus.FAIL) }
                } else {
                    _uiState.update {
                        it.copy(decryptedFirmware = found, status = SherlockStatus.SUCCESS)
                    }
                    addToFirestore(found)
                }
            }
        }
    }

    /**
     * Generate the set of valid 6-char build numbers between [start] and [end]
     * inclusive. Char positions: 0=fixed first letter, 1=bootloader, 2=oneUI,
     * 3=year, 4=month, 5=revision. Skips ASCII 58–64 (the gap between '9' and 'A').
     */
    private fun buildPermutations(
        start: String,
        end: String
    ): Triple<List<String>, List<String>, List<String>> {
        val builds = mutableListOf<String>()
        val cscs = mutableListOf<String>()
        val basebands = mutableListOf<String>()

        fun add(b: Char, v: Char, y: Char, m: Char, r: Char) {
            val core = "$b$v$y$m$r"
            builds += "${start[0]}$core"
            basebands += "${start[0]}$core"
            cscs += core
        }

        for (b in start[1]..end[1]) {
            for (v in start[2]..end[2]) {
                for (y in start[3]..end[3]) {
                    when {
                        y == start[3] -> {
                            for (m in start[4]..end[4]) {
                                when {
                                    m == start[4] -> {
                                        for (r in start[5]..'Z') {
                                            if (r in ':'..'@') continue
                                            add(b, v, y, m, r)
                                            if ("${end[0]}$b$v$y$m$r" == end) break
                                        }
                                    }

                                    m > start[4] && m < end[4] ->
                                        for (r in '1'..'Z') {
                                            if (r in ':'..'@') continue
                                            add(b, v, y, m, r)
                                        }

                                    else ->
                                        for (r in '1'..end[5]) {
                                            if (r in ':'..'@') continue
                                            add(b, v, y, m, r)
                                        }
                                }
                            }
                        }

                        y > start[3] && y < end[3] -> {
                            for (m in 'A'..'L') {
                                for (r in '1'..'Z') {
                                    if (r in ':'..'@') continue
                                    add(b, v, y, m, r)
                                }
                            }
                        }

                        else -> {
                            for (m in 'A'..end[4]) {
                                if (m == end[4]) {
                                    for (r in '1'..end[5]) {
                                        if (r in ':'..'@') continue
                                        add(b, v, y, m, r)
                                    }
                                } else {
                                    for (r in '1'..'Z') {
                                        if (r in ':'..'@') continue
                                        add(b, v, y, m, r)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return Triple(builds, cscs, basebands)
    }

    private fun addToFirestore(decryptedFirmware: String) = viewModelScope.launch(Dispatchers.IO) {
        val prefs = preferenceRepository.getSettings().first()
        if (!prefs.isFirebaseEnabled) return@launch
        val result = searchResult ?: return@launch
        val test = result.firmware.testFirmware

        val docRef = firestore.collection(result.device.model).document(result.device.csc)
        val today = SherlockTools.dateString()

        runCatching {
            if (test.latestFirmware.isBlank()) {
                if (test.clue == "null") {
                    docRef.update(
                        mutableMapOf<String, Any>(
                            "watson" to prefs.profileName,
                            "clue" to decryptedFirmware,
                            "date_latest" to today,
                        ),
                    ).await()
                } else if (test.clue != decryptedFirmware && !SherlockTools.isBetaFirmware(
                        decryptedFirmware
                    )
                ) {
                    if (SherlockTools.compareBuildNumber(
                            SherlockTools.fullBuild(test.clue).takeLast(6),
                            SherlockTools.fullBuild(decryptedFirmware).takeLast(6),
                        ) == 0
                    ) {
                        docRef.update(
                            mutableMapOf<String, Any>(
                                "watson" to prefs.profileName,
                                "clue" to decryptedFirmware,
                                "date_latest" to today,
                            ),
                        ).await()
                    }
                }
            } else {
                docRef.update(
                    mutableMapOf<String, Any>(
                        "watson" to prefs.profileName,
                        "firmware_decrypted" to decryptedFirmware,
                        "date_latest" to today,
                    ),
                ).await()
            }
        }
    }
}
