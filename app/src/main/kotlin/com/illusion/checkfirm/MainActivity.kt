package com.illusion.checkfirm

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme
import com.illusion.checkfirm.core.navigation.EntryProviderInstaller
import com.illusion.checkfirm.core.navigation.NavResultBus
import com.illusion.checkfirm.core.navigation.NavResultKey
import com.illusion.checkfirm.core.navigation.Navigator
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var entryProviderScopes: Set<@JvmSuppressWildcards EntryProviderInstaller>

    @Inject
    lateinit var resultBus: NavResultBus

    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        splashScreen.setKeepOnScreenCondition {
            splashViewModel.isLoading.value
        }

        handleFcmIntent(intent)

        setContent {
            val theme by splashViewModel.appTheme.collectAsStateWithLifecycle()

            CheckFirmTheme(
                theme = theme,
            ) {
                NavDisplay(
                    backStack = navigator.backStack,
                    onBack = navigator::goBack,
                    entryProvider = entryProvider {
                        entryProviderScopes.forEach { builder -> this.builder() }
                    }
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleFcmIntent(intent)
    }

    private fun handleFcmIntent(intent: Intent?) {
        val model = intent?.getStringExtra("new_model") ?: return
        val csc = intent.getStringExtra("new_csc") ?: return
        intent.removeExtra("new_model")
        intent.removeExtra("new_csc")
        lifecycleScope.launch {
            resultBus.emit(NavResultKey.HomeSearch, listOf(model to csc))
        }
    }
}
