package com.illusion.checkfirm.core.navigation

import dagger.hilt.android.scopes.ActivityRetainedScoped
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

/** One queued result stream per destination; a result emitted before Home starts is retained once. */
@ActivityRetainedScoped
class NavResultBus @Inject constructor() {
    private val channels = mutableMapOf<NavResultKey<*>, Channel<Any?>>()
    private fun channel(key: NavResultKey<*>) = channels.getOrPut(key) { Channel(Channel.UNLIMITED) }
    suspend fun <T> emit(key: NavResultKey<T>, value: T) { channel(key).send(value) }
    @Suppress("UNCHECKED_CAST")
    fun <T> asSharedFlow(key: NavResultKey<T>): Flow<T> = channel(key).receiveAsFlow() as Flow<T>
}

sealed interface NavResultKey<T> {
    data object HomeSearch : NavResultKey<List<Pair<String, String>>>
    data object HomeBookmarkPick : NavResultKey<Pair<String, String>>
}
