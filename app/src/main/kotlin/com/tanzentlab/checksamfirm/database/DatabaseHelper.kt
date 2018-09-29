package com.tanzentlab.checksamfirm.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.ArrayList

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    val allBookMark: List<BookMark>
        get() {
            val bookmark = ArrayList<BookMark>()

            val selectQuery = "SELECT * FROM " + BookMark.TABLE_NAME + " ORDER BY " + BookMark.COLUMN_ID + " DESC"

            val db = this.writableDatabase
            val cursor = db.rawQuery(selectQuery, null)

            if (cursor.moveToFirst()) {
                do {
                    val info = BookMark()
                    info.id = cursor.getInt(cursor.getColumnIndex(BookMark.COLUMN_ID))
                    info.name = cursor.getString(cursor.getColumnIndex(BookMark.COLUMN_NAME))
                    info.model = cursor.getString(cursor.getColumnIndex(BookMark.COLUMN_MODEL))
                    info.csc = cursor.getString(cursor.getColumnIndex(BookMark.COLUMN_CSC))

                    bookmark.add(info)
                } while (cursor.moveToNext())
            }
            db.close()

            return bookmark
        }

    val bookMarkCount: Int
        get() {
            val countQuery = "SELECT  * FROM " + BookMark.TABLE_NAME
            val db = this.readableDatabase
            val cursor = db.rawQuery(countQuery, null)

            val count = cursor.count
            cursor.close()

            return count
        }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(BookMark.CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + BookMark.TABLE_NAME)
        onCreate(db)
    }

    fun insertBookMark(name: String, model: String, csc: String): Long {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(BookMark.COLUMN_NAME, name)
        values.put(BookMark.COLUMN_MODEL, model)
        values.put(BookMark.COLUMN_CSC, csc)
        val id = db.insert(BookMark.TABLE_NAME, null, values)
        db.close()

        return id
    }

    fun getBookMark(id: Long): BookMark {
        val db = this.readableDatabase

        val cursor = db.query(BookMark.TABLE_NAME,
                arrayOf(BookMark.COLUMN_ID, BookMark.COLUMN_NAME, BookMark.COLUMN_MODEL, BookMark.COLUMN_CSC),
                BookMark.COLUMN_ID + "=?",
                arrayOf(id.toString()), null, null, null, null)

        cursor?.moveToFirst()

        val bookmark = BookMark(cursor.getInt(cursor.getColumnIndex(BookMark.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(BookMark.COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(BookMark.COLUMN_MODEL)),
                cursor.getString(cursor.getColumnIndex(BookMark.COLUMN_CSC)))

        cursor.close();

        return bookmark
    }

    fun updateBookMark(bookmark: BookMark) {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(BookMark.COLUMN_NAME, bookmark.name)
        values.put(BookMark.COLUMN_MODEL, bookmark.model)
        values.put(BookMark.COLUMN_CSC, bookmark.csc)

        db.update(BookMark.TABLE_NAME, values, BookMark.COLUMN_ID + " = ?", arrayOf(bookmark.id.toString()))
    }

    fun deleteBookMark(bookmark: BookMark) {
        val db = this.writableDatabase
        db.delete(BookMark.TABLE_NAME, BookMark.COLUMN_ID + " = ?", arrayOf(bookmark.id.toString()))
        db.close()
    }

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "bookmark.db"
    }
}