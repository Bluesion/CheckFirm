package com.illusion.checkfirm.feature.sherlock

import com.illusion.checkfirm.core.domain.model.SearchResult

/**
 * Carries the device + firmware payload Sherlock decrypts. The route is reachable
 * from SearchDialog when the test firmware's `latestFirmware` field is encrypted
 * (i.e. lowercase/digit MD5 hash) and the user taps the dynamic action button.
 *
 * `null` is allowed for direct navigation (e.g. from a debug tile) so the screen
 * shows the manual-entry form with empty prefixes.
 */
data class SherlockRouteNavKey(val searchResult: SearchResult? = null)
