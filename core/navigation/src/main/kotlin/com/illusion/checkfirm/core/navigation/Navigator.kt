package com.illusion.checkfirm.core.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.EntryProviderScope
import dagger.hilt.android.scopes.ActivityRetainedScoped

typealias EntryProviderInstaller = EntryProviderScope<Any>.() -> Unit

@ActivityRetainedScoped
class Navigator(startNavKey: Any) {
    val backStack: SnapshotStateList<Any> = mutableStateListOf(startNavKey)

    fun goTo(
        navKey: Any,
        removeFirstScreen: Boolean = false,
    ) {
        backStack.add(navKey)
        if (removeFirstScreen) {
            backStack.removeAt(0)
        }
    }

    fun goBack() {
        backStack.removeLastOrNull()
    }
}
