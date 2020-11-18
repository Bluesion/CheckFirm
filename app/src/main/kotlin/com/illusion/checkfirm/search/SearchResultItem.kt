package com.illusion.checkfirm.search

class SearchResultItem(var officialAndroidVersion: String, var officialLatestFirmware: String, var officialPreviousFirmware: ArrayList<String>,
                       var testAndroidVersion: String, var testLatestFirmware: String, var testPreviousFirmware: ArrayList<String>,
                       var testDiscoveryDate: String, var testType: String, var testDowngrade: String,
                       var testDecrypted: String, var testDiscoverer: String, var testWatson: String)
