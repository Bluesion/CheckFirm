package com.illusion.checkfirm.features

import android.Manifest
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
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat.enableEdgeToEdge
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.firebase.firestore.FirebaseFirestore
import com.illusion.checkfirm.R
import com.illusion.checkfirm.common.theme.CheckFirmTheme
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
import com.illusion.checkfirm.features.main.ui.CategoryDialog
import com.illusion.checkfirm.features.main.ui.MainAdapter
import com.illusion.checkfirm.features.main.ui.NotificationPermissionDialog
import com.illusion.checkfirm.features.main.ui.OutdatedActivity
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
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CheckFirmTheme {
                AppNavigation()
            }
        }
    }
}