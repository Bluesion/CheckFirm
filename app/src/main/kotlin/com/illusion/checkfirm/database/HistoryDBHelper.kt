package com.illusion.checkfirm.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.*

class HistoryDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    val allHistoryDB: List<HistoryDB>
        get() {
            val history = ArrayList<HistoryDB>()
            val selectQuery = "SELECT * FROM " + HistoryDB.TABLE_NAME + " ORDER BY " + HistoryDB.COLUMN_ID + " DESC"
            val db = this.writableDatabase
            val cursor = db.rawQuery(selectQuery, null)

            if (cursor.moveToFirst()) {
                do {
                    val info = HistoryDB()
                    info.id = cursor.getInt(cursor.getColumnIndex(HistoryDB.COLUMN_ID))
                    info.model = cursor.getString(cursor.getColumnIndex(HistoryDB.COLUMN_MODEL))
                    info.csc = cursor.getString(cursor.getColumnIndex(HistoryDB.COLUMN_CSC))
                    info.device = cursor.getString(cursor.getColumnIndex(HistoryDB.COLUMN_DEVICE))
                    info.date = cursor.getString(cursor.getColumnIndex(HistoryDB.COLUMN_DATE))

                    history.add(info)
                } while (cursor.moveToNext())
            }
            db.close()

            return history
        }

    val historyCount: Int
        get() {
            val countQuery = "SELECT  * FROM " + HistoryDB.TABLE_NAME
            val db = this.readableDatabase
            val cursor = db.rawQuery(countQuery, null)

            val count = cursor.count
            cursor.close()

            return count
        }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(HistoryDB.CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + HistoryDB.TABLE_NAME)
        onCreate(db)
    }

    fun insertHistory(model: String, csc: String, device: String, date: String): Long {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(HistoryDB.COLUMN_MODEL, model)
        values.put(HistoryDB.COLUMN_CSC, csc)
        values.put(HistoryDB.COLUMN_DEVICE, device)
        values.put(HistoryDB.COLUMN_DATE, date)
        val id = db.insertWithOnConflict(HistoryDB.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE)
        db.close()

        return id
    }

    fun getHistory(id: Long): HistoryDB {
        val db = this.readableDatabase

        val cursor = db.query(HistoryDB.TABLE_NAME,
                arrayOf(HistoryDB.COLUMN_ID, HistoryDB.COLUMN_MODEL, HistoryDB.COLUMN_CSC,
                        HistoryDB.COLUMN_DEVICE, HistoryDB.COLUMN_DATE), HistoryDB.COLUMN_ID + "=?",
                arrayOf(id.toString()), null, null, null, null)

        cursor?.moveToFirst()

        val history = HistoryDB(cursor.getInt(cursor.getColumnIndex(HistoryDB.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(HistoryDB.COLUMN_MODEL)),
                cursor.getString(cursor.getColumnIndex(HistoryDB.COLUMN_CSC)),
                cursor.getString(cursor.getColumnIndex(HistoryDB.COLUMN_DEVICE)),
                cursor.getString(cursor.getColumnIndex(HistoryDB.COLUMN_DATE)))

        cursor.close();

        return history
    }

    fun updateHistory(History: HistoryDB) {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(HistoryDB.COLUMN_MODEL, History.model)
        values.put(HistoryDB.COLUMN_CSC, History.csc)
        values.put(HistoryDB.COLUMN_DEVICE, History.device)
        values.put(HistoryDB.COLUMN_DATE, History.date)

        db.update(HistoryDB.TABLE_NAME, values, HistoryDB.COLUMN_ID + " = ?", arrayOf(History.id.toString()))
    }

    fun deleteHistory(History: HistoryDB) {
        val db = this.writableDatabase
        db.delete(HistoryDB.TABLE_NAME, HistoryDB.COLUMN_ID + " = ?", arrayOf(History.id.toString()))
        db.close()
    }

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "search_history.db"
    }
}