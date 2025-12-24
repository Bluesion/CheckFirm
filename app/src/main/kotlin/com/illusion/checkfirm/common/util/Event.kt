package com.illusion.checkfirm.common.util

/**
 * A generic wrapper for events that can only be consumed once. (Prevents multiple consumptions of the same event)
 */
open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its further use.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content even if it's already been handled.
     */
    fun peekContent(): T = content
}