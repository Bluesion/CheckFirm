package com.illusion.checkfirm.search

class HistoryItem {

    private var model = "SM-"
    private var csc = ""
    private var date = ""

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

    fun getDate(): String {
        return date
    }

    fun setDate(date: String) {
        this.date = date
    }
}