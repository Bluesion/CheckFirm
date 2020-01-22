package com.illusion.checkfirm.search

class SearchItem {
    private var model = "SM-"
    private var csc = ""

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
}