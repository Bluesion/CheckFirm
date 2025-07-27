package com.illusion.checkfirm.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.illusion.checkfirm.data.model.BCDao
import com.illusion.checkfirm.data.model.BookmarkEntity
import com.illusion.checkfirm.data.model.CategoryEntity

@Database(entities = [BookmarkEntity::class, CategoryEntity::class], version = 3, exportSchema = false)
abstract class BCDatabase : RoomDatabase() {

    abstract fun bcDao(): BCDao

    companion object {
        @Volatile
        private var INSTANCE: BCDatabase? = null

        fun getDatabase(context: Context): BCDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, BCDatabase::class.java, "bookmarks")
                        .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                        .build()
                INSTANCE = instance
                instance
            }
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE bookmark_info ADD COLUMN category TEXT NOT NULL DEFAULT ''")
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("DROP TABLE IF EXISTS bookmark_info_new")
                db.execSQL("CREATE TABLE bookmark_info_new (id INTEGER, name TEXT NOT NULL, model TEXT NOT NULL, csc TEXT NOT NULL, device TEXT NOT NULL, category TEXT NOT NULL, position INTEGER NOT NULL, PRIMARY KEY(id))")
                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_bookmark_info_name_device_v3 ON bookmark_info_new (name, device)")
                db.execSQL("INSERT INTO bookmark_info_new SELECT id, name, model, csc, device, '', 0 FROM bookmark_info")
                db.execSQL("ALTER TABLE bookmark_info RENAME TO bookmark_info_old")
                db.execSQL("ALTER TABLE bookmark_info_new RENAME TO bookmark_info")
                db.execSQL("DROP TABLE IF EXISTS bookmark_info_old")

                db.execSQL("DROP TABLE IF EXISTS category_info")
                db.execSQL("CREATE TABLE category_info (id INTEGER, name TEXT NOT NULL, position INTEGER NOT NULL, PRIMARY KEY(id))")
                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_category_info_name ON category_info (name)")
            }
        }
    }
}