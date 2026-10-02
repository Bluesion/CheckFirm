package com.illusion.checkfirm.feature.settings.backuprestore

import com.illusion.checkfirm.core.domain.model.*
import com.illusion.checkfirm.core.domain.repository.BCRepository
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.*
import kotlinx.serialization.json.Json
import org.junit.Assert.*
import org.junit.Test

class BackupItemTest {
    private val json = Json { encodeDefaults = true; ignoreUnknownKeys = true }
    private val device = Device("SM-S928B", "EUX")
    @Test fun legacyXmlExportPreservesIdentityPositionAndLogicalDevice() {
        val source = """{"bookmarkList":[{"id":42,"name":"Long bookmark name","model":"SM-S928B","csc":"EUX","category":"Phones","device":"DeviceItem(model=SM-S928B, csc=EUX)","position":7}],"categoryList":[{"id":9,"name":"Phones","position":3}]}"""
        val item = json.decodeFromString(BackupItem.serializer(), source)
        val bookmark = item.bookmarkList.single().toDomain()
        assertEquals(42L, bookmark.id)
        assertEquals(7, bookmark.position)
        assertEquals(device, bookmark.device)
        assertEquals(item, json.decodeFromString(BackupItem.serializer(), json.encodeToString(BackupItem.serializer(), item)))
        assertEquals(BookmarkDto.fromDomain(bookmark), item.bookmarkList.single())
        assertEquals(CategoryDto.fromDomain(item.categoryList.single().toDomain()), item.categoryList.single())
    }
    @Test fun minimalOlderExportsStillDecode() {
        val item = json.decodeFromString(BackupItem.serializer(), """{"bookmarkList":[{"name":"Phone","model":"SM-S928B","csc":"EUX","category":""}]}""")
        assertNull(item.bookmarkList.single().id)
        assertEquals(0, item.bookmarkList.single().position)
    }
    @Test fun restoringIntoEmptyDatabaseRetainsOriginalIdsAndOrder() = runBlocking {
        val repository = FakeRepository()
        val item = BackupItem(listOf(BookmarkDto("newer", "SM-X710", "KOO", "", 52, position = 2), BookmarkDto("older", device.model, device.csc, "Phones", 42, position = 1)), listOf(CategoryDto("Phones", 9, 3)))
        restoreBackup(repository, item)
        assertEquals(listOf(42L,52L), repository.bookmarks.map { it.id })
        assertEquals(listOf(1,2), repository.bookmarks.map { it.position })
        assertEquals(9L, repository.categories.single().id)
    }
    @Test fun mergingCannotOverwriteUnrelatedRowsAndReimportKeepsLocalIdentity() = runBlocking {
        val repository = FakeRepository()
        repository.bookmarks.add(Bookmark("unrelated", Device("SM-A720S","SKC"), "", 42, 0))
        val backup = BackupItem(listOf(BookmarkDto("imported",device.model,device.csc,"",42, position = 7)))
        restoreBackup(repository, backup)
        assertEquals(2, repository.bookmarks.size)
        assertEquals("unrelated", repository.bookmarks.first().name)
        val importedId = repository.bookmarks.last().id
        assertNotEquals(42L, importedId)
        restoreBackup(repository, backup)
        assertEquals(2, repository.bookmarks.size)
        assertEquals(importedId, repository.bookmarks.last().id)
        assertEquals(7, repository.bookmarks.last().position)
    }
    @Test fun multipleNamedBookmarksForOneDeviceRetainDistinctIdsOnReimport() = runBlocking {
        val repository = FakeRepository()
        val backup = BackupItem(listOf(BookmarkDto("first alias",device.model,device.csc,"",1), BookmarkDto("second alias",device.model,device.csc,"",2)))
        restoreBackup(repository, backup)
        restoreBackup(repository, backup)
        assertEquals(listOf(1L, 2L), repository.bookmarks.map { it.id })
        assertEquals(listOf("first alias", "second alias"), repository.bookmarks.map { it.name })
    }
    private class FakeRepository : BCRepository {
        val bookmarks = mutableListOf<Bookmark>()
        val categories = mutableListOf<Category>()
        override fun getAllBookmark(order: String, isDesc: Boolean) = flowOf(bookmarks.toList())
        override fun getBookmarkByCategory(order: String,isDesc: Boolean,category: String) = flowOf(bookmarks.filter { it.category == category })
        override suspend fun addBookmark(bookmark: Bookmark) { bookmarks.add(bookmark.copy(id = bookmark.id ?: ((bookmarks.maxOfOrNull { it.id ?: 0 } ?: 0) + 1))) }
        override suspend fun editBookmark(bookmark: Bookmark) { bookmarks[bookmarks.indexOfFirst { it.id == bookmark.id }] = bookmark }
        override suspend fun deleteBookmark(device: Device) { bookmarks.removeAll { it.device == device } }
        override suspend fun deleteAllBookmark() { bookmarks.clear() }
        override fun getAllCategory() = flowOf(categories.toList())
        override suspend fun addCategory(category: Category) { categories.add(category.copy(id = category.id ?: 1)) }
        override suspend fun editCategory(category: Category) { categories[categories.indexOfFirst { it.id == category.id }] = category }
        override suspend fun deleteCategory(name: String) { categories.removeAll { it.name == name } }
        override suspend fun deleteAllCategory() { categories.clear() }
    }
}
