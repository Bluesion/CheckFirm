package com.illusion.checkfirm.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.*

class BookmarkDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    val allBookmarkDB: List<BookmarkDB>
        get() {
            val bookmark = ArrayList<BookmarkDB>()
            val selectQuery = "SELECT * FROM " + BookmarkDB.TABLE_NAME + " ORDER BY " + BookmarkDB.COLUMN_ID + " DESC"
            val db = this.writableDatabase
            val cursor = db.rawQuery(selectQuery, null)

            if (cursor.moveToFirst()) {
                do {
                    val info = BookmarkDB()
                    info.id = cursor.getInt(cursor.getColumnIndex(BookmarkDB.COLUMN_ID))
                    info.name = cursor.getString(cursor.getColumnIndex(BookmarkDB.COLUMN_NAME))
                    info.model = cursor.getString(cursor.getColumnIndex(BookmarkDB.COLUMN_MODEL))
                    info.csc = cursor.getString(cursor.getColumnIndex(BookmarkDB.COLUMN_CSC))

                    bookmark.add(info)
                } while (cursor.moveToNext())
            }
            db.close()

            return bookmark
        }

    val bookMarkCount: Int
        get() {
            val countQuery = "SELECT  * FROM " + BookmarkDB.TABLE_NAME
            val db = this.readableDatabase
            val cursor = db.rawQuery(countQuery, null)

            val count = cursor.count
            cursor.close()

            return count
        }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(BookmarkDB.CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + BookmarkDB.TABLE_NAME)
        onCreate(db)
    }

    fun insertBookMark(name: String, model: String, csc: String): Long {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(BookmarkDB.COLUMN_NAME, name)
        values.put(BookmarkDB.COLUMN_MODEL, model)
        values.put(BookmarkDB.COLUMN_CSC, csc)
        val id = db.insert(BookmarkDB.TABLE_NAME, null, values)
        db.close()

        return id
    }

    fun getBookMark(id: Long): BookmarkDB {
        val db = this.readableDatabase

        val cursor = db.query(BookmarkDB.TABLE_NAME,
                arrayOf(BookmarkDB.COLUMN_ID, BookmarkDB.COLUMN_NAME, BookmarkDB.COLUMN_MODEL, BookmarkDB.COLUMN_CSC),
                BookmarkDB.COLUMN_ID + "=?",
                arrayOf(id.toString()), null, null, null, null)

        cursor?.moveToFirst()

        val bookmark = BookmarkDB(cursor.getInt(cursor.getColumnIndex(BookmarkDB.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(BookmarkDB.COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(BookmarkDB.COLUMN_MODEL)),
                cursor.getString(cursor.getColumnIndex(BookmarkDB.COLUMN_CSC)))

        cursor.close();

        return bookmark
    }

    fun updateBookMark(bookmark: BookmarkDB) {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(BookmarkDB.COLUMN_NAME, bookmark.name)
        values.put(BookmarkDB.COLUMN_MODEL, bookmark.model)
        values.put(BookmarkDB.COLUMN_CSC, bookmark.csc)

        db.update(BookmarkDB.TABLE_NAME, values, BookmarkDB.COLUMN_ID + " = ?", arrayOf(bookmark.id.toString()))
    }

    fun deleteBookMark(bookmark: BookmarkDB) {
        val db = this.writableDatabase
        db.delete(BookmarkDB.TABLE_NAME, BookmarkDB.COLUMN_ID + " = ?", arrayOf(bookmark.id.toString()))
        db.close()
    }

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "bookmark.db"
    }
}