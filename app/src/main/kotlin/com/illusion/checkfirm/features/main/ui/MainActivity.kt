package com.illusion.checkfirm.features.main.ui

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.firebase.firestore.FirebaseFirestore
import com.illusion.checkfirm.R
import com.illusion.checkfirm.common.ui.base.CheckFirmActivity
import com.illusion.checkfirm.common.ui.recyclerview.RecyclerViewVerticalMarginDecorator
import com.illusion.checkfirm.common.util.Tools
import com.illusion.checkfirm.data.model.local.DeviceItem
import com.illusion.checkfirm.data.model.local.SearchResultItem
import com.illusion.checkfirm.data.model.remote.ApiResponse
import com.illusion.checkfirm.data.model.remote.AppVersionStatus
import com.illusion.checkfirm.data.source.remote.FirmwareFetcher
import com.illusion.checkfirm.databinding.ActivityMainBinding
import com.illusion.checkfirm.features.bookmark.ui.BookmarkCategoryActivity
import com.illusion.checkfirm.features.bookmark.viewmodel.BookmarkViewModel
import com.illusion.checkfirm.features.catcher.ui.InfoCatcherActivity
import com.illusion.checkfirm.features.main.viewmodel.AppMetadataViewModel
import com.illusion.checkfirm.features.search.ui.SearchActivity
import com.illusion.checkfirm.features.search.ui.SearchDialog
import com.illusion.checkfirm.features.settings.SettingsActivity
import com.illusion.checkfirm.features.settings.help.MyDeviceActivity
import com.illusion.checkfirm.features.settings.viewmodel.SettingsViewModel
import com.illusion.checkfirm.features.welcome.ui.WelcomeSearchActivity
import com.illusion.checkfirm.features.welcome.viewmodel.WelcomeSearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : CheckFirmActivity<ActivityMainBinding>() {

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                    showNotificationPermissionRationale()
                }
            }
        }

    private lateinit var fwFetcher: FirmwareFetcher

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data = result.data
            if (result.resultCode == Activity.RESULT_OK) {
                when (data?.getIntExtra("search_code", -1)) {
                    0 -> {
                        val modelString = data.getStringExtra("model")
                        val cscString = data.getStringExtra("csc")

                        if (modelString == null || cscString == null) {
                            val modelArray = data.getStringArrayExtra("model") as Array<String>
                            val cscArray = data.getStringArrayExtra("csc") as Array<String>
                            searchFirmware(modelArray, cscArray)
                        } else {
                            searchFirmware(modelString, cscString)
                        }
                    }

                    else -> {
                        showMainLayout(SEARCH_ERROR_LAYOUT)
                    }
                }
            }
        }

    private val appMetadataViewModel by viewModels<AppMetadataViewModel>()

    private val settingsViewModel by viewModels<SettingsViewModel>()

    private val bookmarkViewModel by viewModels<BookmarkViewModel>()

    private val wsViewModel by viewModels<WelcomeSearchViewModel>()

    private var currentCategory = ""

    override fun createBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun setContentInset() {
        setBottomInset(binding.mainView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        initToolbar(binding.includeToolbar.appBar, getString(R.string.app_name))
        supportActionBar?.title = ""
        binding.includeToolbar.toolbar.navigationIcon = null

        if (Build.VERSION.SDK_INT >= 33) {
            notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }

        initFetcher()

        lifecycleScope.launch {
            val firstState = appMetadataViewModel.isOldVersion.first { it !is ApiResponse.Loading }
            when (firstState) {
                is ApiResponse.Error -> {
                    if (firstState is ApiResponse.Error.NetworkError) {
                        showMainLayout(NETWORK_ERROR_LAYOUT)
                    } else {
                        showMainLayout(HELLO_LAYOUT)
                    }
                    splashScreen.setKeepOnScreenCondition { false }
                }

                is ApiResponse.Success -> {
                    splashScreen.setKeepOnScreenCondition { false }

                    when (firstState.data) {
                        // If update required, open outdated activity
                        AppVersionStatus.UPDATE_REQUIRED -> {
                            startActivity(Intent(this@MainActivity, OutdatedActivity::class.java))
                            finish()
                        }
                        // Currently, do nothing if it's latest or old version
                        else -> {
                            if (settingsViewModel.settingsState.drop(1).first().isWelcomeSearchEnabled) {
                                welcomeSearch()
                            } else {
                                startSearch()
                            }
                        }
                    }
                }

                else -> {
                    // NO-OP (Should not happen after flow cancel(first()))
                }
            }

            repeatOnLifecycle(Lifecycle.State.STARTED) {
                settingsViewModel.settingsState.collect {
                    if (it.isQuickSearchBarEnabled) {
                        initQuickSearchBar()
                    } else {
                        binding.quickSearchBar.visibility = View.GONE
                    }

                    (binding.searchResult.adapter as MainAdapter).updateFirebaseStatus(it.isFirebaseEnabled)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> {
                startForResult.launch(Intent(this, SearchActivity::class.java).apply {
                    putExtra("search_code", 0)
                })
                return true
            }

            R.id.bookmark -> {
                startForResult.launch(Intent(this, BookmarkCategoryActivity::class.java).apply {
                    putExtra("search_code", 0)
                })
                return true
            }

            R.id.settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initFetcher() {
        fwFetcher = FirmwareFetcher(FirebaseFirestore.getInstance())

        binding.searchResult.apply {
            addItemDecoration(
                RecyclerViewVerticalMarginDecorator(
                    Tools.dpToPx(this@MainActivity, 12f)
                )
            )
            adapter = MainAdapter(
                emptyList(),
                onCardClicked = { isOfficial, searchResult ->
                    SearchDialog(isOfficial, searchResult).show(supportFragmentManager, null)
                },
                onCardLongClicked = { firmware ->
                    (getSystemService(CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(
                        ClipData.newPlainText(
                            "CheckFirm",
                            firmware
                        )
                    )
                    Toast.makeText(this@MainActivity, R.string.clipboard, Toast.LENGTH_SHORT).show()
                }
            )
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun initQuickSearchBar() {
        binding.categoryIcon.setOnClickListener {
            CategoryDialog(
                category = currentCategory,
                onCategoryClicked = {
                    currentCategory = it
                    bookmarkViewModel.updateCategory(it)
                }
            ).show(supportFragmentManager, null)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                bookmarkViewModel.bookmarkList.collect { bookmarkList ->
                    if (bookmarkList.isEmpty()) {
                        binding.quickSearchBar.visibility = View.GONE
                    } else {
                        binding.chipGroup.removeAllViews()
                        for (bookmark in bookmarkList) {
                            Chip(this@MainActivity).apply {
                                text = bookmark.name
                                setOnClickListener {
                                    searchFirmware(bookmark.model, bookmark.csc)
                                }
                            }.also { binding.chipGroup.addView(it) }
                        }
                        binding.quickSearchBar.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun startSearch() {
        val model = Build.MODEL
        val csc = Tools.getCSC()

        if (Tools.isValidDevice(model, csc)) {
            searchFirmware(model, csc)
        } else {
            // Show hello layout for non-samsung devices
            showMainLayout(HELLO_LAYOUT)
        }
    }

    private suspend fun welcomeSearch() {
        val welcomeSearchDeviceList = wsViewModel.allDevices.first()
        if (welcomeSearchDeviceList.isEmpty()) {
            showMainLayout(WELCOME_DEVICE_EMPTY_LAYOUT)
        } else {
            val modelArray = Array(welcomeSearchDeviceList.size) { welcomeSearchDeviceList[it].model }
            val cscArray = Array(welcomeSearchDeviceList.size) { welcomeSearchDeviceList[it].csc }

            searchFirmware(modelArray, cscArray)
        }
    }

    private fun searchFirmware(modelArray: Array<String>, cscArray: Array<String>) {
        if (Tools.isOnline(this)) {
            showMainLayout(LOADING_LAYOUT)

            lifecycleScope.launch {
                val searchResultList = mutableListOf<SearchResultItem>()

                for (i in 0 until modelArray.size) {
                    val model = modelArray[i].uppercase().trim()
                    val csc = cscArray[i].uppercase().trim()

                    if (Tools.isValidDevice(model, csc)) {
                        searchResultList.add(
                            fwFetcher.search(
                                DeviceItem(model, csc),
                                settingsViewModel.settingsState.first().isFirebaseEnabled,
                                settingsViewModel.settingsState.first().profileName
                            )
                        )
                    }
                }

                binding.loadingProgress.visibility = View.GONE
                binding.errorView.visibility = View.GONE
                (binding.searchResult.adapter as MainAdapter).updateLists(searchResultList)
            }
        } else {
            showMainLayout(NETWORK_ERROR_LAYOUT)
        }
    }

    private fun searchFirmware(model: String, csc: String) {
        searchFirmware(arrayOf(model), arrayOf(csc))
    }

    private fun showNotificationPermissionRationale() {
        NotificationPermissionDialog(
            onPositiveButtonClicked = {
                if (Build.VERSION.SDK_INT >= 33) {
                    notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        ).show(supportFragmentManager, null)
    }

    private fun showMainLayout(layout: Int) {
        when (layout) {
            WELCOME_DEVICE_EMPTY_LAYOUT -> {
                binding.errorTitle.text = getString(R.string.welcome_search)
                binding.errorDescription.text = getString(R.string.main_welcome_search_text)
                binding.errorTip1.apply {
                    text = getString(R.string.suggestion_enable_welcome_search)
                    setOnClickListener {
                        startActivity(Intent(this@MainActivity, WelcomeSearchActivity::class.java))
                    }
                }
                binding.errorTip2.visibility = View.GONE
                binding.loadingProgress.visibility = View.GONE
                binding.helloView.visibility = View.GONE
                binding.errorView.visibility = View.VISIBLE
            }

            NETWORK_ERROR_LAYOUT -> {
                binding.errorTitle.text = getString(R.string.main_network_error_title)
                binding.errorDescription.text = getString(R.string.main_network_error_text)
                binding.errorTip1.apply {
                    text = getString(R.string.suggestion_wifi)
                    setOnClickListener {
                        startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                    }
                }
                binding.errorTip2.apply {
                    text = getString(R.string.suggestion_data)
                    visibility = View.VISIBLE
                    setOnClickListener {
                        startActivity(Intent(Settings.ACTION_DATA_USAGE_SETTINGS))
                    }
                }
                binding.loadingProgress.visibility = View.GONE
                binding.helloView.visibility = View.GONE
                binding.errorView.visibility = View.VISIBLE
            }

            SEARCH_ERROR_LAYOUT -> {
                binding.errorTitle.text = getString(R.string.main_search_error_title)
                binding.errorDescription.text = getString(R.string.main_search_error_text)
                binding.errorTip1.apply {
                    text = getString(R.string.suggestion_check_my_device_info)
                    setOnClickListener {
                        startActivity(Intent(this@MainActivity, MyDeviceActivity::class.java))
                    }
                }
                binding.errorTip2.visibility = View.GONE
                binding.loadingProgress.visibility = View.GONE
                binding.helloView.visibility = View.GONE
                binding.errorView.visibility = View.VISIBLE
            }

            HELLO_LAYOUT -> {
                binding.helloSearchLayout.setOnClickListener {
                    startForResult.launch(Intent(this, SearchActivity::class.java).apply {
                        putExtra("search_code", 0)
                    })
                }
                binding.helloBookmarkLayout.setOnClickListener {
                    startForResult.launch(Intent(this, BookmarkCategoryActivity::class.java).apply {
                        putExtra("search_code", 0)
                    })
                }
                binding.helloWelcomeSearchLayout.setOnClickListener {
                    startActivity(Intent(this, WelcomeSearchActivity::class.java))
                }
                binding.helloInfoCatcherLayout.setOnClickListener {
                    startActivity(Intent(this, InfoCatcherActivity::class.java))
                }
                binding.helloSettingsLayout.setOnClickListener {
                    startActivity(Intent(this, SettingsActivity::class.java))
                }
                binding.loadingProgress.visibility = View.GONE
                binding.helloView.visibility = View.VISIBLE
                binding.errorView.visibility = View.GONE
            }

            else -> {
                binding.loadingProgress.visibility = View.VISIBLE
                binding.errorView.visibility = View.GONE
                binding.helloView.visibility = View.GONE
            }
        }
    }

    companion object {
        private const val LOADING_LAYOUT = 0
        private const val WELCOME_DEVICE_EMPTY_LAYOUT = 1
        private const val NETWORK_ERROR_LAYOUT = 2
        private const val SEARCH_ERROR_LAYOUT = 3
        private const val HELLO_LAYOUT = 4
    }
}