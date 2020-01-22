package com.illusion.checkfirm.main

class PreviousItem {
    private var officialPrevious = ""
    private var testPrevious = ""

    fun getOfficialPrevious(): String {
        return officialPrevious
    }

    fun setOfficialPrevious(previous: String) {
        this.officialPrevious = previous
    }

    fun getTestPrevious(): String {
        return testPrevious
    }

    fun setTestPrevious(previous: String) {
        this.testPrevious = previous
    }
}