package com.illusion.checkfirm.main

class SmartItem {
    private var date = ""
    private var downgrade = ""
    private var changelog = ""

    fun getDate(): String {
        return date
    }

    fun setDate(date: String) {
        this.date = date
    }

    fun getDowngrade(): String {
        return downgrade
    }

    fun setDowngrade(downgrade: String) {
        this.downgrade = downgrade
    }

    fun getChangelog(): String {
        return changelog
    }

    fun setChangelog(changelog: String) {
        this.changelog = changelog
    }
}