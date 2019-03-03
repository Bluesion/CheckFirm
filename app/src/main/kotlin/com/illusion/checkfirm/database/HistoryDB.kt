package com.illusion.checkfirm.database

class HistoryDB {
    var id: Int = 0
    var model: String? = null
    var csc: String? = null
    var device: String? = null
    var date: String? = null

    internal constructor()

    internal constructor(id: Int, model: String, csc: String, device: String, date: String) {
        this.id = id
        this.model = model
        this.csc = csc
        this.device = device
        this.date = date
    }

    companion object {
        const val TABLE_NAME = "history"

        const val COLUMN_ID = "_id"
        const val COLUMN_MODEL = "model"
        const val COLUMN_CSC = "csc"
        const val COLUMN_DEVICE = "device"
        const val COLUMN_DATE = "date"

        const val CREATE_TABLE = (
                "CREATE TABLE " + TABLE_NAME + "("
                        + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + COLUMN_MODEL + " TEXT, "
                        + COLUMN_CSC + " TEXT, "
                        + COLUMN_DEVICE + " TEXT UNIQUE, "
                        + COLUMN_DATE + " TEXT)")
    }
}
