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
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.illusion.checkfirm.CheckFirm
import com.illusion.checkfirm.R
import com.illusion.checkfirm.common.ui.base.CheckFirmActivity
import com.illusion.checkfirm.features.bookmark.ui.BookmarkCategoryActivity
import com.illusion.checkfirm.features.catcher.ui.InfoCatcherActivity
import com.illusion.checkfirm.features.welcome.viewmodel.WelcomeSearchViewModel
import com.illusion.checkfirm.databinding.ActivityMainBinding
import com.illusion.checkfirm.features.main.viewmodel.MainViewModel
import com.illusion.checkfirm.data.source.remote.FWFetcher
import com.illusion.checkfirm.features.search.ui.SearchActivity
import com.illusion.checkfirm.features.settings.SettingsActivity
import com.illusion.checkfirm.features.settings.help.MyDeviceActivity
import com.illusion.checkfirm.common.ui.recyclerview.RecyclerViewVerticalMarginDecorator
import com.illusion.checkfirm.common.util.Tools
import com.illusion.checkfirm.features.bookmark.viewmodel.BookmarkViewModel
import com.illusion.checkfirm.features.search.ui.SearchDialog
import com.illusion.checkfirm.features.welcome.ui.WelcomeSearchActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : CheckFirmActivity<ActivityMainBinding>() {

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                    showNotificationPermissionRationale()
                }
            }
        }

    private lateinit var fwFetcher: FWFetcher

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data = result.data
            if (result.resultCode == Activity.RESULT_OK) {
                when (data?.getIntExtra("search_code", -1)) {
                    0 -> {
                        val model = data.getStringExtra("model") as String
                        val csc = data.getStringExtra("csc") as String
                        val total = data.getIntExtra("total", 1)

                        searchFirmware(model, csc, total)
                    }

                    else -> {
                        showMainLayout(3)
                    }
                }
            }
        }

    private val mainViewModel: MainViewModel by viewModels()
    private val bookmarkViewModel: BookmarkViewModel by viewModels()
    private val wsViewModel: WelcomeSearchViewModel by viewModels()
    private var currentCategory = ""

    override fun createBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun setContentInset() {
        setBottomInset(binding.mainView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        initToolbar(binding.includeToolbar.appBar, getString(R.string.app_name))
        supportActionBar?.title = ""
        binding.includeToolbar.toolbar.navigationIcon = null

        if (Build.VERSION.SDK_INT >= 33) {
            notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }

        fwFetcher = FWFetcher(this@MainActivity)

        lifecycleScope.launch {
            binding.searchResult.apply {
                addItemDecoration(
                    RecyclerViewVerticalMarginDecorator(
                        Tools.dpToPx(this@MainActivity, 12f)
                    )
                )
                adapter = MainAdapter(
                    total = 0,
                    isFirebaseEnabled = settingsViewModel.getAllSettings().isFirebaseEnabled,
                    onCardClicked = { isOfficial, position ->
                        SearchDialog(isOfficial, position).show(supportFragmentManager, null)
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

            val isConnectedToInternet = Tools.isOnline(this@MainActivity)
            if (isConnectedToInternet) {
                initQuickSearchBar()
                if (settingsViewModel.getAllSettings().isWelcomeSearchEnabled) {
                    welcomeSearch()
                } else {
                    startSearch()
                }
            } else {
                showMainLayout(2)
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

    private suspend fun initQuickSearchBar() {
        if (settingsViewModel.getAllSettings().isQuickSearchBarEnabled) {
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
                                        searchFirmware(bookmark.model, bookmark.csc, 1)
                                    }
                                }.also { binding.chipGroup.addView(it) }
                            }
                            binding.quickSearchBar.visibility = View.VISIBLE
                        }
                    }
                }
            }
        } else {
            binding.quickSearchBar.visibility = View.GONE
        }
    }

    private fun startSearch() {
        val model = Build.MODEL
        val csc = Tools.getCSC()

        if (Tools.isValidDevice(model, csc)) {
            searchFirmware(model, csc, 1)
        } else {
            showMainLayout(4)
        }
    }

    private fun welcomeSearch() {
        lifecycleScope.launch(Dispatchers.Main) {
            val welcomeSearchDeviceList = wsViewModel.allDevices.first()
            if (welcomeSearchDeviceList.isEmpty()) {
                showMainLayout(1)
            } else {
                var model = ""
                var csc = ""

                for (element in welcomeSearchDeviceList) {
                    model += "${element.model}%"
                    csc += "${element.csc}%"
                }
                searchFirmware(model, csc, welcomeSearchDeviceList.size)
            }
        }
    }

    private fun searchFirmware(tempModel: String, tempCSC: String, total: Int) {
        if (Tools.isOnline(this)) {
            showMainLayout(0)

            when {
                total == 1 -> {
                    val model = if (tempModel.endsWith("%")) {
                        tempModel.substring(0, tempModel.length - 1)
                    } else {
                        tempModel
                    }

                    val csc = if (tempCSC.endsWith("%")) {
                        tempCSC.substring(0, tempCSC.length - 1)
                    } else {
                        tempCSC
                    }

                    CheckFirm.searchModel[0] = model.uppercase().trim()
                    CheckFirm.searchCSC[0] = csc.uppercase().trim()
                }

                total > 1 -> {
                    val modelList = tempModel.split("%")
                    val cscList = tempCSC.split("%")

                    for (i in 0 until modelList.size - 1) {
                        CheckFirm.searchModel[i] = modelList[i].uppercase().trim()
                        CheckFirm.searchCSC[i] = cscList[i].uppercase().trim()
                    }
                }
            }

            lifecycleScope.launch(Dispatchers.Main) {
                if (fwFetcher.start(total) == 0) {
                    showSearchResult(total)
                } else {
                    showMainLayout(3)
                }
            }
        } else {
            showMainLayout(2)
        }
    }

    private fun showSearchResult(total: Int) {
        binding.loadingProgress.visibility = View.GONE
        binding.errorView.visibility = View.GONE
        (binding.searchResult.adapter as MainAdapter).updateTotal(total)
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

    /**
     * code 0: Loading Layout
     * code 1: No Welcome Layout
     * code 2: Network Error Layout
     * code 3: Search Error Layout
     * code 4: Hello Layout
     */
    private fun showMainLayout(code: Int) {
        when (code) {
            1 -> {
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

            2 -> {
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

            3 -> {
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

            4 -> {
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
}