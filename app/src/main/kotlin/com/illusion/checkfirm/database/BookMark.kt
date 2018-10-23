package com.illusion.checkfirm.database

class BookMark {

    var id: Int = 0
    var name: String? = null
    var model: String? = null
    var csc: String? = null

    internal constructor()

    internal constructor(id: Int, name: String, model: String, csc: String) {
        this.id = id
        this.name = name
        this.model = model
        this.csc = csc
    }

    companion object {
        const val TABLE_NAME = "bookmark"

        const val COLUMN_ID = "_id"
        const val COLUMN_NAME = "name"
        const val COLUMN_MODEL = "model"
        const val COLUMN_CSC = "csc"

        const val CREATE_TABLE = (
                "CREATE TABLE " + TABLE_NAME + "("
                        + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + COLUMN_NAME + " TEXT, "
                        + COLUMN_MODEL + " TEXT, "
                        + COLUMN_CSC + " TEXT)")
    }
}
