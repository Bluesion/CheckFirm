package com.illusion.checkfirm.features

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen : NavKey {
    @Serializable
    data object Main : Screen

    @Serializable
    data object Search : Screen

    @Serializable
    data object Bookmark : Screen

    @Serializable
    data object Preference : Screen
}

@Composable
fun MainNavigation() {
    val backStack = rememberNavBackStack(Screen.Main)

    NavDisplay(
        backStack = backStack,
        entryProvider = entryProvider {
            entry<Screen.Main> {
                MainScreen(
                    onScreenStart = {},
                    onScreenEnd = {},
                    navigateToSearch = { backStack.add(Screen.Search) },
                    navigateToBookmark = { backStack.add(Screen.Bookmark) },
                    navigateToPreference = { backStack.add(Screen.Preference) }
                )
            }
        }
    )
}