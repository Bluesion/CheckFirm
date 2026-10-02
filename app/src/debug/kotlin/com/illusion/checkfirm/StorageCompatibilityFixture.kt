package com.illusion.checkfirm

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.room.Room
import com.illusion.checkfirm.core.database.*
import com.illusion.checkfirm.core.database.entity.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

/** Uses only disposable in-memory databases, never the application's stored records. */
@Composable
internal fun StorageCompatibilityFixture() {
    val context = LocalContext.current
    var status by remember { mutableStateOf("Storage checks running") }
    LaunchedEffect(Unit) {
        status = try {
            withContext(Dispatchers.IO) {
                val bookmarks = Room.inMemoryDatabaseBuilder(context, BCDatabase::class.java).build()
                val welcome = Room.inMemoryDatabaseBuilder(context, WelcomeSearchDatabase::class.java).build()
                val catcher = Room.inMemoryDatabaseBuilder(context, InfoCatcherDatabase::class.java).build()
                try {
                    val dao = bookmarks.bcDao()
                    dao.addBookmark(BookmarkEntity(42, "legacy", "SM-S928B", "EUX", "DeviceItem(model=SM-S928B, csc=EUX)", "Phones", 7))
                    dao.addBookmark(BookmarkEntity(43, "unrelated", "SM-X710", "KOO", "Device(model=SM-X710, csc=KOO)", "", 0))
                    dao.editBookmark(BookmarkEntity(42, "renamed", "SM-S928B", "EUX", "DeviceItem(model=SM-S928B, csc=EUX)", "Phones", 7))
                    check(dao.getAllBookmark("time", false).first().first().let { it.id == 42L && it.position == 7 && it.name == "renamed" })
                    dao.addCategory(CategoryEntity(9, "Phones", 3))
                    dao.deleteCategory("Phones")
                    check(dao.getAllCategory().first().isEmpty())
                    check(dao.getAllBookmark("time", false).first().first().category.isEmpty())
                    check(dao.deleteBookmark("SM-S928B", "EUX") == 1)
                    check(dao.getAllBookmark("time", false).first().single().name == "unrelated")
                    check(dao.deleteBookmark("SM-X710", "KOO") == 1)
                    welcome.welcomeSearchDao().insert(WelcomeSearchEntity(1, "SM-S928B", "EUX", "DeviceItem(model=SM-S928B, csc=EUX)"))
                    check(welcome.welcomeSearchDao().delete("SM-S928B", "EUX") == 1)
                    catcher.infoCatcherDao().insert(InfoCatcherEntity(1, "SM-S928B", "EUX", "DeviceItem(model=SM-S928B, csc=EUX)"))
                    check(catcher.infoCatcherDao().delete("SM-S928B", "EUX") == 1)
                    "Storage checks passed: 8"
                } finally {
                    bookmarks.close(); welcome.close(); catcher.close()
                }
            }
        } catch (error: Exception) { "Storage checks failed: ${error.message}" }
    }
    Text(status)
}
