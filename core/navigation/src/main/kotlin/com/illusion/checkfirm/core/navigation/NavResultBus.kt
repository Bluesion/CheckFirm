package com.illusion.checkfirm.core.navigation

import dagger.hilt.android.scopes.ActivityRetainedScoped
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Activity-scoped bus that decouples a producer screen (e.g. Search picking a device)
 * from a consumer screen (e.g. Home running the firmware lookup). Each result type is
 * keyed so that unrelated subscribers don't collide.
 *
 * Producer:  bus.emit(NavResultKey.HomeSearch, payload)
 * Consumer:  bus.flow<Payload>(NavResultKey.HomeSearch).collect { ... }
 */
@ActivityRetainedScoped
class NavResultBus @Inject constructor() {

    private val flows = mutableMapOf<NavResultKey<*>, MutableSharedFlow<Any?>>()

    suspend fun <T> emit(key: NavResultKey<T>, value: T) {
        flow(key).emit(value)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> flow(key: NavResultKey<T>): MutableSharedFlow<T> =
        flows.getOrPut(key) {
            MutableSharedFlow<Any?>(extraBufferCapacity = 1)
        } as MutableSharedFlow<T>

    fun <T> asSharedFlow(key: NavResultKey<T>): SharedFlow<T> = flow(key).asSharedFlow()
}

sealed interface NavResultKey<T> {
    /** A list of (model, csc) pairs picked in Search to be looked up on Home. */
    data object HomeSearch : NavResultKey<List<Pair<String, String>>>

    /** A single (model, csc) chosen from Bookmark to be looked up on Home. */
    data object HomeBookmarkPick : NavResultKey<Pair<String, String>>
}
