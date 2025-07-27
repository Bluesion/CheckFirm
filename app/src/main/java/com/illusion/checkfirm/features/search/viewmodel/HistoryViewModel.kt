package com.illusion.checkfirm.features.search.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.illusion.checkfirm.data.model.HistoryEntity
import com.illusion.checkfirm.data.model.SearchDeviceItem
import com.illusion.checkfirm.data.repository.HistoryRepository
import com.illusion.checkfirm.data.source.local.HistoryDatabase
import kotlinx.coroutines.launch
import java.util.Calendar

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: HistoryRepository

    init {
        val historyDao = HistoryDatabase.getDatabase(application).historyDao()
        repository = HistoryRepository(historyDao)
    }

    suspend fun getAllHistoryList(): List<HistoryEntity> {
        return repository.getAllHistoryList()
    }

    fun cleanUpHistory() = viewModelScope.launch {
        repository.cleanUpHistory()
    }

    fun createHistory(list: List<SearchDeviceItem>) {
        insert(list)
    }

    fun insert(list: List<SearchDeviceItem>) = viewModelScope.launch {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val previousHistoryList = getAllHistoryList()

        for (deviceItem in list) {
            val model = deviceItem.device.model
            val csc = deviceItem.device.csc

            for (history in previousHistoryList) {
                if (history.model == model && history.csc == csc) {
                    delete(model, csc)
                }
            }

            repository.insert(HistoryEntity(null, model, csc, year, month, day))
        }

        cleanUpHistory()
    }

    fun update(id: Long, model: String, csc: String, year: Int, month: Int, day: Int) = viewModelScope.launch {
        repository.update(HistoryEntity(id, model, csc, year, month, day))
    }

    fun delete(model: String, csc: String) = viewModelScope.launch {
        repository.delete(model, csc)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }
}