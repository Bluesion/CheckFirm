package com.illusion.checkfirm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme
import com.illusion.checkfirm.core.navigation.EntryProviderInstaller
import com.illusion.checkfirm.core.navigation.Navigator
import com.illusion.checkfirm.core.preference.api.PreferenceRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var entryProviderScopes: Set<@JvmSuppressWildcards EntryProviderInstaller>

    @Inject
    lateinit var preferenceRepository: PreferenceRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val theme = runBlocking {
            preferenceRepository.getSettings().first().theme
        }

        setContent {
            CheckFirmTheme(
                darkTheme = theme == "dark"
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
}