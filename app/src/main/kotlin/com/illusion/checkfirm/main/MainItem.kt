package com.illusion.checkfirm.main

class MainItem {

    private var model = ""
    private var csc = ""
    private var officialLatest = ""
    private var testLatest = ""

    fun getModel(): String {
        return model
    }

    fun setModel(model: String) {
        this.model = model
    }

    fun getCsc(): String {
        return csc
    }

    fun setCsc(csc: String) {
        this.csc = csc
    }

    fun getOfficialLatest(): String {
        return officialLatest
    }

    fun setOfficialLatest(latest: String) {
        this.officialLatest = latest
    }

    fun getTestLatest(): String {
        return testLatest
    }

    fun setTestLatest(latest: String) {
        this.testLatest = latest
    }
}