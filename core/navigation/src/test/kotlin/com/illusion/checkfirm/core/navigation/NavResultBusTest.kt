package com.illusion.checkfirm.core.navigation

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import org.junit.Assert.*
import org.junit.Test

class NavResultBusTest {
    @Test fun coldStartAndPoppedEntryResultsAreDeliveredExactlyOnce() = runBlocking {
        val bus = NavResultBus()
        val value = listOf("SM-S928B" to "EUX")
        bus.emit(NavResultKey.HomeSearch, value)
        assertEquals(value, withTimeout(1000) { bus.asSharedFlow(NavResultKey.HomeSearch).first() })
        assertNull(withTimeoutOrNull(30) { bus.asSharedFlow(NavResultKey.HomeSearch).first() })
        bus.emit(NavResultKey.HomeSearch, value)
        assertEquals(value, withTimeout(1000) { bus.asSharedFlow(NavResultKey.HomeSearch).first() })
    }
    @Test fun multipleResultsRetainOrderAndDifferentKeysDoNotStealResults() = runBlocking {
        val bus = NavResultBus()
        bus.emit(NavResultKey.HomeSearch, listOf("first" to "EUX"))
        bus.emit(NavResultKey.HomeBookmarkPick, "bookmark" to "KOO")
        bus.emit(NavResultKey.HomeSearch, listOf("second" to "XAA"))
        assertEquals("first", bus.asSharedFlow(NavResultKey.HomeSearch).first().single().first)
        assertEquals("second", bus.asSharedFlow(NavResultKey.HomeSearch).first().single().first)
        assertEquals("bookmark", bus.asSharedFlow(NavResultKey.HomeBookmarkPick).first().first)
    }
    @Test fun backCannotRemoveTheRootEntry() {
        val navigator = Navigator("home")
        navigator.goBack()
        assertEquals(listOf("home"), navigator.backStack.toList())
        navigator.goTo("search")
        navigator.goBack()
        navigator.goBack()
        assertEquals(listOf("home"), navigator.backStack.toList())
    }
}
