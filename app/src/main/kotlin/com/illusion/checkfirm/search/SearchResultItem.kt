package com.illusion.checkfirm.search

class SearchResultItem(var officialAndroidVersion: String, var officialLatestFirmware: String, var officialPreviousFirmware: HashMap<String, String>,
                       var testAndroidVersion: String, var testLatestFirmware: String, var testPreviousFirmware: HashMap<String, String>,
                       var testDiscoveryDate: String, var testType: String, var testDowngrade: String,
                       var testDecrypted: String, var testDiscoverer: String, var testWatson: String)
