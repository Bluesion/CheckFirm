package com.illusion.checkfirm.features.sherlock.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.illusion.checkfirm.CheckFirm
import com.illusion.checkfirm.common.util.Tools
import com.illusion.checkfirm.data.source.local.SettingsDataSource
import com.illusion.checkfirm.features.sherlock.util.SherlockStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.ArrayList
import java.util.Calendar

class SherlockViewModel(application: Application) : AndroidViewModel(application) {

    private var i = 0
    private val _buildPrefix = MutableStateFlow("")
    val buildPrefix: StateFlow<String> = _buildPrefix.asStateFlow()
    private val _cscPrefix = MutableStateFlow("")
    val cscPrefix: StateFlow<String> = _cscPrefix.asStateFlow()
    private val _basebandPrefix = MutableStateFlow("")
    val basebandPrefix: StateFlow<String> = _basebandPrefix.asStateFlow()

    private val _manualBuild = MutableStateFlow("")
    val manualBuild: StateFlow<String> = _manualBuild.asStateFlow()
    private val _manualCsc = MutableStateFlow("")
    val manualCsc: StateFlow<String> = _manualCsc.asStateFlow()
    private val _manualBaseband = MutableStateFlow("")
    val manualBaseband: StateFlow<String> = _manualBaseband.asStateFlow()

    private val scriptBuildList = ArrayList<String>()
    private val scriptCscList = ArrayList<String>()
    private val scriptBasebandList = ArrayList<String>()

    private val _scriptStart = MutableStateFlow("")
    val scriptStart: StateFlow<String> = _scriptStart.asStateFlow()

    private val _scriptEnd = MutableStateFlow("")
    val scriptEnd: StateFlow<String> = _scriptEnd.asStateFlow()

    private val _userInput = MutableStateFlow("")
    val userInput: StateFlow<String> = _userInput.asStateFlow()

    private val appSettingsRepository: SettingsDataSource = SettingsDataSource(application)
    private var isFirebaseEnabled = false
    private var profileName = "Unknown"

    private val _sherlockStatus = MutableStateFlow(SherlockStatus.INITIAL)
    val sherlockStatus: StateFlow<SherlockStatus> = _sherlockStatus.asStateFlow()

    val db = FirebaseFirestore.getInstance()

    init {
        viewModelScope.launch {
            isFirebaseEnabled = appSettingsRepository.isFirebaseEnabled.first()
            profileName = appSettingsRepository.getProfileName.first()
        }
    }

    fun initialize(currentIndex: Int) {
        i = currentIndex
        val officialFirmware = CheckFirm.firmwareItems[i].testFirmwareItem.clue.ifBlank {
            CheckFirm.firmwareItems[i].officialFirmwareItem.latestFirmware
        }

        val firstIndex = officialFirmware.indexOf("/")
        if (firstIndex == -1) {
            val prefix = "${CheckFirm.searchModel[i].substring(3)}XX"
            val dummy = "U0A${getDate()}1"

            _buildPrefix.value = prefix
            _manualBuild.value = dummy

            _cscPrefix.value = prefix
            _manualCsc.value = dummy

            _basebandPrefix.value = prefix
            _manualBaseband.value = dummy
        } else {
            val secondIndex = officialFirmware.lastIndexOf("/")
            val buildVersion = officialFirmware.substring(0, firstIndex)
            val cscVersion = officialFirmware.substring(firstIndex + 1, secondIndex)
            var basebandInfo = ""

            val length = buildVersion.length
            val buildPrefix = buildVersion.substring(0, length - 6)
            val cscPrefix = cscVersion.substring(0, length - 5)
            var basebandPrefix = ""
            if (officialFirmware.length - 1 > secondIndex) {
                val basebandVersion = officialFirmware.substring(secondIndex + 1)
                basebandPrefix = basebandVersion.substring(0, length - 6)
                basebandInfo = basebandVersion.substring(length - 6)
            }

            _buildPrefix.value = buildPrefix
            _manualBuild.value = buildVersion.substring(length - 6)

            _cscPrefix.value = cscPrefix
            _manualCsc.value = cscVersion.substring(length - 5)

            _basebandPrefix.value = basebandPrefix
            _manualBaseband.value = basebandInfo
        }

        _scriptStart.value = _manualBuild.value
        _scriptEnd.value = "${_manualBuild.value.substring(0, 3)}${getDate()}Z"

        _sherlockStatus.value = SherlockStatus.INITIAL
        _userInput.value = ""
    }

    fun setManualBuildPrefix(prefix: String) {
        _buildPrefix.value = prefix
        compare()
    }

    fun setManualCscPrefix(prefix: String) {
        _cscPrefix.value = prefix
        compare()
    }

    fun setManualBasebandPrefix(prefix: String) {
        _basebandPrefix.value = prefix
        compare()
    }

    fun setManualBuild(build: String) {
        _manualBuild.value = build
        compare()
    }

    fun setManualCsc(csc: String) {
        _manualCsc.value = csc
        compare()
    }

    fun setManualBaseband(baseband: String) {
        _manualBaseband.value = baseband
        compare()
    }

    fun setScriptStart(script: String) {
        if (Tools.isCorrectBuildNumber(script)) {
            _scriptStart.value = script
            validateScript()
        } else {
            _sherlockStatus.value = SherlockStatus.WARNING_SCRIPT_START_INVALID
        }
    }

    fun setScriptEnd(script: String) {
        if (Tools.isCorrectBuildNumber(script)) {
            _scriptEnd.value = script
            validateScript()
        } else {
            _sherlockStatus.value = SherlockStatus.WARNING_SCRIPT_END_INVALID
        }
    }

    fun setSherlockStatus(status: SherlockStatus) {
        _sherlockStatus.value = status
    }

    private fun getDate(): String {
        val calendar = Calendar.getInstance()
        val todayYear = calendar.get(Calendar.YEAR)
        val todayMonth = calendar.get(Calendar.MONTH).plus(1)

        val yearSub = todayYear - 2011
        val monthSub = todayMonth - 1

        val year = (75 + yearSub).toChar()
        val month = (65 + monthSub).toChar()

        return "$year$month"
    }

    fun compare() {
        _userInput.value = "${_buildPrefix.value}${_manualBuild.value}/${_cscPrefix.value}${_manualCsc.value}/${_basebandPrefix.value}${_manualBaseband.value}"
        viewModelScope.launch {
            val encryptedFirmware = Tools.getMD5Hash(_userInput.value)

            if (CheckFirm.firmwareItems[i].testFirmwareItem.latestFirmware.isBlank()) {
                if (CheckFirm.firmwareItems[i].testFirmwareItem.previousFirmware[encryptedFirmware] != null) {
                    _sherlockStatus.value = SherlockStatus.SUCCESS
                    addToFireStore()
                } else {
                    _sherlockStatus.value = SherlockStatus.FAIL
                }
            } else {
                if (CheckFirm.firmwareItems[i].testFirmwareItem.latestFirmware == encryptedFirmware) {
                    _sherlockStatus.value = SherlockStatus.SUCCESS
                    addToFireStore()
                } else {
                    _sherlockStatus.value = SherlockStatus.FAIL
                }
            }
        }
    }

    fun validateScript() {
        if (Tools.isCorrectBuildNumber(_scriptStart.value) && Tools.isCorrectBuildNumber(_scriptEnd.value)) {
            viewModelScope.launch {
                when (Tools.compareBuildNumber(_scriptStart.value, _scriptEnd.value)) {
                    0 -> {
                        _sherlockStatus.value = SherlockStatus.NO_WARNING
                    }
                    1, 2 -> {
                        _sherlockStatus.value = SherlockStatus.WARNING_BUILD_NUMBER_BOOTLOADER
                    }
                    3 -> {
                        _sherlockStatus.value = SherlockStatus.WARNING_BUILD_NUMBER_ONE_UI_VERSION
                    }
                    4 -> {
                        _sherlockStatus.value = SherlockStatus.WARNING_BUILD_NUMBER_YEAR
                    }
                    5 -> {
                        _sherlockStatus.value = SherlockStatus.WARNING_BUILD_NUMBER_MONTH
                    }
                    else -> {
                        _sherlockStatus.value = SherlockStatus.WARNING_BUILD_NUMBER_REVISION
                    }
                }
            }
        } else if (!Tools.isCorrectBuildNumber(_scriptStart.value)) {
            _sherlockStatus.value = SherlockStatus.WARNING_SCRIPT_START_INVALID
        } else {
            _sherlockStatus.value = SherlockStatus.WARNING_SCRIPT_END_INVALID
        }
    }

    fun runScript() {
        _sherlockStatus.value = SherlockStatus.RUNNING
        viewModelScope.launch(Dispatchers.Default) {
            var latestValue: String? = null
            initArrayList()
            val stringBuilder = StringBuilder()
            for (x in 0 until scriptBuildList.size) {
                for (y in 0..x) {
                    for (z in 0..x) {
                        stringBuilder.append(_buildPrefix.value)
                        stringBuilder.append(scriptBuildList[x])
                        stringBuilder.append("/")
                        stringBuilder.append(_cscPrefix.value)
                        stringBuilder.append(scriptCscList[y])
                        stringBuilder.append("/")
                        stringBuilder.append(_basebandPrefix.value)
                        stringBuilder.append(scriptBasebandList[z])

                        val currentValue = stringBuilder.toString()

                        val encryptedFirmware =
                            Tools.getMD5Hash(currentValue)

                        if (CheckFirm.firmwareItems[i].testFirmwareItem.latestFirmware.isBlank()) {
                            when {
                                CheckFirm.firmwareItems[i].testFirmwareItem.previousFirmware[encryptedFirmware] != null -> {
                                    latestValue = currentValue
                                }
                            }
                        } else {
                            when (CheckFirm.firmwareItems[i].testFirmwareItem.latestFirmware) {
                                encryptedFirmware -> {
                                    latestValue = currentValue
                                }
                            }
                        }
                        stringBuilder.setLength(0)
                    }
                }
            }

            withContext(Dispatchers.Main) {
                if (latestValue == null) {
                    _sherlockStatus.value = SherlockStatus.FAIL
                } else {
                    _userInput.value = latestValue
                    _sherlockStatus.value = SherlockStatus.SUCCESS
                    addToFireStore()
                }
            }
        }
    }

    private fun initArrayList() {
        scriptBuildList.clear()
        scriptCscList.clear()
        scriptBasebandList.clear()

        // b stands for bootloader, v for one ui version, y for year, m for month, r for revision
        for (b in _scriptStart.value[1].._scriptEnd.value[1]) {
            for (v in _scriptStart.value[2].._scriptEnd.value[2]) {
                for (y in _scriptStart.value[3].._scriptEnd.value[3]) {
                    when {
                        y == _scriptStart.value[3] -> {
                            for (m in _scriptStart.value[4].._scriptEnd.value[4]) {
                                when {
                                    m == _scriptStart.value[4] -> {
                                        for (r in _scriptStart.value[5]..'Z') {
                                            if (r !in ':'..'@') {
                                                addToArrayList(b, v, y, m, r)
                                                if ("${_scriptEnd.value[0]}$b$v$y$m$r" == _scriptEnd.value) {
                                                    break
                                                }
                                            }
                                        }
                                    }
                                    m > _scriptStart.value[4] && m < _scriptEnd.value[4] -> {
                                        for (r in '1'..'Z') {
                                            if (r !in ':'..'@') {
                                                addToArrayList(b, v, y, m, r)
                                            }
                                        }
                                    }
                                    else -> {
                                        for (r in '1'.._scriptEnd.value[5]) {
                                            if (r !in ':'..'@') {
                                                addToArrayList(b, v, y, m, r)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        y > _scriptStart.value[3] && y < _scriptEnd.value[3] -> {
                            for (m in 'A'..'L') {
                                for (r in '1'..'Z') {
                                    if (r !in ':'..'@') {
                                        addToArrayList(b, v, y, m, r)
                                    }
                                }
                            }
                        }
                        else -> {
                            for (m in 'A'.._scriptEnd.value[4]) {
                                if (m == _scriptEnd.value[4]) {
                                    for (r in '1'.._scriptEnd.value[5]) {
                                        if (r !in ':'..'@') {
                                            addToArrayList(b, v, y, m, r)
                                        }
                                    }
                                } else {
                                    for (r in '1'..'Z') {
                                        if (r !in ':'..'@') {
                                            addToArrayList(b, v, y, m, r)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun addToArrayList(b: Char, v: Char, y: Char, m: Char, r: Char) {
        val stringBuilder = StringBuilder()
        stringBuilder.append(_scriptStart.value[0])
        stringBuilder.append(b)
        stringBuilder.append(v)
        stringBuilder.append(y)
        stringBuilder.append(m)
        stringBuilder.append(r)

        scriptBuildList.add(stringBuilder.toString())
        scriptBasebandList.add(stringBuilder.toString())

        stringBuilder.deleteAt(0)
        scriptCscList.add(stringBuilder.toString())
    }

    fun addToFireStore() {
        if (isFirebaseEnabled) {
            val model = CheckFirm.searchModel[i]
            val csc = CheckFirm.searchCSC[i]

            viewModelScope.launch(Dispatchers.IO) {
                val docRef = db.collection(model).document(csc)

                if (CheckFirm.firmwareItems[i].testFirmwareItem.latestFirmware.isBlank()) {
                    if (CheckFirm.firmwareItems[i].testFirmwareItem.clue == "null") {
                        docRef.update(
                            mutableMapOf<String, Any>(
                                "watson" to profileName,
                                "clue" to _userInput.value,
                                "date_latest" to Tools.dateToString(Tools.getCurrentDateTime())
                            )
                        ).await()
                        CheckFirm.firmwareItems[i].testFirmwareItem.watson = profileName
                        CheckFirm.firmwareItems[i].testFirmwareItem.clue = _userInput.value
                        CheckFirm.firmwareItems[i].testFirmwareItem.discoveryDate = Tools.dateToString(Tools.getCurrentDateTime())
                    } else {
                        if ((CheckFirm.firmwareItems[i].testFirmwareItem.clue != _userInput.value) && (Tools.getFirmwareInfo(
                                _userInput.value
                            )[2] != 'Z')
                        ) {
                            if (Tools.compareFirmware(
                                    CheckFirm.firmwareItems[i].testFirmwareItem.clue,
                                    _userInput.value
                                ) == 0
                            ) {
                                docRef.update(
                                    mutableMapOf<String, Any>(
                                        "watson" to profileName,
                                        "clue" to _userInput.value,
                                        "date_latest" to Tools.dateToString(Tools.getCurrentDateTime())
                                    )
                                ).await()
                                CheckFirm.firmwareItems[i].testFirmwareItem.watson = profileName
                                CheckFirm.firmwareItems[i].testFirmwareItem.clue = _userInput.value
                                CheckFirm.firmwareItems[i].testFirmwareItem.discoveryDate = Tools.dateToString(Tools.getCurrentDateTime())
                            }
                        }
                    }
                } else {
                    docRef.update(
                        mutableMapOf<String, Any>(
                            "watson" to profileName,
                            "firmware_decrypted" to _userInput.value,
                            "date_latest" to Tools.dateToString(Tools.getCurrentDateTime())
                        )
                    ).await()
                    CheckFirm.firmwareItems[i].testFirmwareItem.watson = profileName
                    CheckFirm.firmwareItems[i].testFirmwareItem.clue = _userInput.value
                    CheckFirm.firmwareItems[i].testFirmwareItem.discoveryDate = Tools.dateToString(Tools.getCurrentDateTime())
                }
            }
        }
    }
}