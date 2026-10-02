package com.illusion.checkfirm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.material3.Surface
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme
import com.illusion.checkfirm.core.domain.model.*
import com.illusion.checkfirm.core.preference.api.Preference
import com.illusion.checkfirm.feature.home.*
import com.illusion.checkfirm.feature.search.*
import com.illusion.checkfirm.feature.bookmark.impl.*
import com.illusion.checkfirm.feature.category.impl.*
import com.illusion.checkfirm.feature.report.*
import com.illusion.checkfirm.feature.sherlock.*
import com.illusion.checkfirm.feature.settings.*
import com.illusion.checkfirm.feature.settings.about.*
import com.illusion.checkfirm.feature.settings.backuprestore.*
import com.illusion.checkfirm.feature.settings.help.*
import com.illusion.checkfirm.feature.settings.help.mydevice.*
import com.illusion.checkfirm.feature.settings.help.firmware.*
import com.illusion.checkfirm.feature.settings.welcome.*
import com.illusion.checkfirm.feature.settings.catcher.*

/** Debug-only, deterministic rendering; never reads or writes production records or calls a service. */
class MigrationFixtureActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val screen = intent.getStringExtra("screen") ?: "home"
        val populated = intent.getBooleanExtra("populated", false)
        val theme = intent.getStringExtra("theme") ?: "light"
        setContent { CheckFirmTheme(theme) { Surface { Fixture(screen, populated, theme) { finish() } } } }
    }
}

private val devices = listOf(Device("SM-S928B", "EUX"), Device("SM-X710", "KOO"), Device("SM-F946B", "SEK"), Device("SM-A720S", "SKC"), Device("SM-S918B", "XAA"))
private val bookmarksFixture = devices.mapIndexed { i, d ->
    Bookmark(if (i == 0) "Galaxy S24 Ultra with an intentionally long bookmark name" else "Fixture device ${i + 1}", d,
        if (i < 2) "Phones" else "", id = i.toLong() + 1, position = i)
}
private val resultFixture = SearchResult(devices.first(), Firmware(
    OfficialFirmware("S928BXXU3AXL5/S928BOXM3AXL5/S928BXXU3AXL5", mapOf("20241001" to "S928BXXU2AXJ2/S928BOXM2AXJ2/S928BXXU2AXJ2"), "Galaxy S24 Ultra", "2024-12-05", "14"),
    TestFirmware(latestFirmware = "0123456789abcdef0123456789abcdef", previousFirmware = mapOf("20241101" to "S928BXXU3AXK1/S928BOXM3AXK1/S928BXXU3AXK1"),
        betaFirmware = mapOf("20241201" to "S928BXXU3ZXL1/S928BOXM3ZXL1/S928BXXU3ZXL1"), discoveryDate = "2024-12-09", androidVersion = "15", discoverer = "Fixture discoverer with a long name"),
))

@Composable
private fun Fixture(screen: String, populated: Boolean, theme: String, back: () -> Unit) {
    val bookmarks = if (populated) bookmarksFixture.asReversed() else emptyList()
    val categories = if (populated) listOf(Category("Phones", 1, 0), Category("A long category name for truncation checks", 2, 1)) else emptyList()
    var preference by remember { mutableStateOf(Preference(theme = theme, isFirebaseEnabled = false, isQuickSearchBarEnabled = populated, profileName = if (populated) "Fixture user with a long profile name" else "Unknown")) }
    var dialog by remember { mutableStateOf(when (screen) {
        "profile" -> PreferenceDialog.Profile; "theme" -> PreferenceDialog.Theme; "language" -> PreferenceDialog.Language;
        "order" -> PreferenceDialog.BookmarkOrder; "reset" -> PreferenceDialog.BookmarkReset; else -> PreferenceDialog.None
    }) }
    var search by remember { mutableStateOf(SearchUiState()) }
    var bookmarkState by remember { mutableStateOf(BookmarkUiState(bookmarks = bookmarks, categories = categories, showNewBookmark = screen == "bookmarkdialog")) }
    var openedResult by remember { mutableStateOf<SearchResult?>(if (screen == "firmware") resultFixture else null) }
    var openedOfficial by remember { mutableStateOf(true) }
    var reportState by remember { mutableStateOf(ReportUiState(userMessage = if (populated) "Fixture report with enough text to verify multiline input and wrapping." else "", bugTypes = if (populated) setOf(BugType.FIRMWARE_INFO_ERROR) else emptySet())) }
    when (screen) {
        "storage" -> StorageCompatibilityFixture()
        "lifetime" -> NavigationLifetimeFixture()
        "home", "firmware", "network" -> HomeScreen(
            uiState = HomeUiState(preference = preference.copy(isQuickSearchBarEnabled = populated), bookmarks = bookmarks, categories = categories,
                results = if (populated) listOf(resultFixture, resultFixture.copy(device = devices[1])) else emptyList(),
                resultState = if (screen == "network") ResultState.NetworkError else if (populated) ResultState.Success else ResultState.Idle,
                openedDialog = openedResult, openedDialogIsOfficial = openedOfficial),
            visibleBookmarks = bookmarks, onSearchIconClick = {}, onBookmarkIconClick = {}, onPreferenceIconClick = {},
            onWelcomeSearchClick = {}, onInfoCatcherClick = {}, onCategoryIconClick = {}, onCategoryPick = {}, onBookmarkChipClick = {},
            onCategoryDialogDismiss = {}, onResultClick = { result, official -> openedResult = result; openedOfficial = official }, onResultDismiss = { openedResult = null }, onCopy = {}, onOpenOfficialDoc = {}, onOpenSherlock = {}, onOpenReport = {}, onOpenFirmwareManual = {},
        )
        "search" -> SearchScreen(uiState = search, onModelChange = { search = search.copy(model = it) }, onCscChange = { search = search.copy(csc = it) }, onDeviceClick = { device -> search = search.copy(searchList = if (search.searchList.any { it.device == device }) search.searchList.filterNot { it.device == device } else search.searchList + SearchDeviceItem(device)); com.illusion.checkfirm.feature.search.util.SearchValidationResult.SUCCESS }, onRemoveFromSearchList = { device -> search = search.copy(searchList = search.searchList.filterNot { it.device == device }) }, historyList = if (populated) devices.mapIndexed { i, d -> SearchHistory(d, Date(2026, 9, 24 + i)) }.asReversed() else emptyList(), bookmarks = bookmarks, onNavigationIconClick = back)
        "bookmark", "bookmarkdialog", "category" -> BookmarkScreen(
            uiState = bookmarkState, initialTab = if (screen == "category") 1 else 0,
            onExpandedChange = { bookmarkState = bookmarkState.copy(expanded = it) }, onCategoryChange = { selected -> bookmarkState = bookmarkState.copy(selectedCategory = selected, bookmarks = bookmarks.filter { selected.isBlank() || it.category == selected }) }, onEditingBookmarkChange = { bookmarkState = bookmarkState.copy(editingBookmark = it) }, onShowNewBookmarkChange = { bookmarkState = bookmarkState.copy(showNewBookmark = it) }, onAddBookmark = {}, onEditBookmark = {}, onDeleteBookmark = {}, onItemClick = {}, onNavigationIconClick = back,
        )
        "categoryedit" -> {
            var state by remember { mutableStateOf(CategoryEditUiState(name = if (populated) "Phones" else "", bookmarks = bookmarks, selected = bookmarks.filter { it.category == "Phones" }.map { it.deviceKey() }.toSet())) }
            CategoryScreen(state, { state = state.copy(name = it) }, { b -> state = state.copy(selected = if (b.deviceKey() in state.selected) state.selected - b.deviceKey() else state.selected + b.deviceKey()) }, {}, back)
        }
        "welcome", "welcomedialog" -> WelcomeSearchScreen(uiState = WelcomeSearchUiState(isWelcomeSearchEnabled = false, devices = if (populated) devices.sortedBy { it.storageKey } else emptyList(), bookmarks = bookmarks, showDialog = screen == "welcomedialog"), onNavigationIconClick = back)
        "catcher", "catcherdialog" -> InfoCatcherScreen(uiState = InfoCatcherUiState(devices = if (populated) devices.sortedBy { it.storageKey } else emptyList(), bookmarks = bookmarks, showDialog = screen == "catcherdialog"), onNavigationIconClick = back, onEnableChange = {}, onAddDeviceClick = {}, onDeleteDevice = {}, onDialogDismiss = back, onDialogModelChange = {}, onDialogCscChange = {}, onSelectBookmark = {}, onAddDevice = { _, _ -> })
        "report" -> ReportScreen(reportState, { bug -> reportState = reportState.copy(bugTypes = if (bug in reportState.bugTypes) reportState.bugTypes - bug else reportState.bugTypes + bug) }, { reportState = reportState.copy(userMessage = it) }, {}, back)
        "sherlock" -> {
            var state by remember { mutableStateOf(SherlockUiState(buildPrefix = "S928BXX", cscPrefix = "S928BOXM", basebandPrefix = "S928BXX", manualBuild = "U3AXL5", manualCsc = "3AXL5", manualBaseband = "U3AXL5", scriptStart = "U3AXL5", scriptEnd = "U3A${"ZJ"}Z")) }
            SherlockScreen(state, back, onTabChange = { state = state.copy(selectedTab = it) }, onShowInfo = { state = state.copy(showInfoDialog = true) }, onDismissInfo = { state = state.copy(showInfoDialog = false) })
        }
        "help" -> HelpScreen(onNavigateBack = back, onNavigateToFirmwareManual = {}, onNavigateToMyDevice = {})
        "manual" -> FirmwareManualScreen(onNavigationIconClick = back)
        "mydevice" -> MyDeviceScreen(MyDeviceUiState(userName = "Fixture user with a long profile name", deviceName = "Fixture Galaxy", model = devices.first().model, csc = devices.first().csc), back)
        "backup" -> BackupRestoreScreen(BackupRestoreUiState(), onNavigateBack = back)
        "about", "legal", "contributor" -> AboutScreen(AboutUiState(activeDialog = when (screen) { "legal" -> DialogType.LEGAL; "contributor" -> DialogType.CONTRIBUTOR; else -> DialogType.NONE }, versionCheck = VersionCheckState.NetworkError), back, {}, back)
        else -> PreferenceScreen(
            PreferenceUiState(preference, dialog), {}, {}, {}, {}, {}, back, { dialog = it },
            { preference = preference.copy(profileName = it) }, { preference = preference.copy(theme = it) }, { preference = preference.copy(language = it) },
            { preference = preference.copy(isQuickSearchBarEnabled = it) }, { order, asc -> preference = preference.copy(bookmarkOrder = order, isBookmarkAscOrder = asc) },
            { preference = preference.copy(isWelcomeSearchEnabled = it) }, { preference = preference.copy(isInfoCatcherEnabled = it) }, { preference = preference.copy(isFirebaseEnabled = it) }, {},
        )
    }
}
