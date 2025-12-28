package com.illusion.checkfirm.features.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.illusion.checkfirm.data.model.local.HistoryEntity
import com.illusion.checkfirm.data.model.local.SearchDeviceItem
import com.illusion.checkfirm.data.repository.HistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val historyRepository: HistoryRepository
) : ViewModel() {

    suspend fun getAllHistoryList(): List<HistoryEntity> {
        return historyRepository.getAllHistoryList()
    }

    fun cleanUpHistory() = viewModelScope.launch {
        historyRepository.cleanUpHistory()
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

            historyRepository.insert(HistoryEntity(null, model, csc, year, month, day))
        }

        cleanUpHistory()
    }

    fun update(id: Long, model: String, csc: String, year: Int, month: Int, day: Int) =
        viewModelScope.launch {
            historyRepository.update(HistoryEntity(id, model, csc, year, month, day))
        }

    fun delete(model: String, csc: String) = viewModelScope.launch {
        historyRepository.delete(model, csc)
    }

    fun deleteAll() = viewModelScope.launch {
        historyRepository.deleteAll()
    }
}