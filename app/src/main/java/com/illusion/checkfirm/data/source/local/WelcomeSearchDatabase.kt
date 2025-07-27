package com.illusion.checkfirm.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.illusion.checkfirm.data.model.WelcomeSearchDao
import com.illusion.checkfirm.data.model.WelcomeSearchEntity

@Database(entities = [WelcomeSearchEntity::class], version = 1, exportSchema = false)
abstract class WelcomeSearchDatabase : RoomDatabase() {

    abstract fun welcomeSearchDao(): WelcomeSearchDao

    companion object {
        @Volatile
        private var INSTANCE: WelcomeSearchDatabase? = null

        fun getDatabase(context: Context): WelcomeSearchDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WelcomeSearchDatabase::class.java,
                    "welcome_search_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}