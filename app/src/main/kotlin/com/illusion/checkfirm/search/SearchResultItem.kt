package com.illusion.checkfirm.search

import java.util.*

class SearchResultItem(var officialAndroidVersion: String, var officialLatestFirmware: String, var officialPreviousFirmware: SortedMap<String, String>,
                       var testAndroidVersion: String, var testLatestFirmware: String, var testPreviousFirmware: SortedMap<String, String>,
                       var testDiscoveryDate: String, var testType: String, var testDowngrade: String,
                       var testDecrypted: String, var testDiscoverer: String, var testWatson: String)
