package com.illusion.checkfirm.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.illusion.checkfirm.domain.model.Date
import com.illusion.checkfirm.domain.model.Device
import com.illusion.checkfirm.domain.model.SearchHistory
import com.illusion.checkfirm.domain.repository.HistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val historyRepository: HistoryRepository
) : ViewModel() {

    val historyList: StateFlow<List<SearchHistory>> = historyRepository.getAllHistory()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    suspend fun getAllHistoryList(): List<SearchHistory> {
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
                if (history.device.model == model && history.device.csc == csc) {
                    delete(model, csc)
                }
            }

            historyRepository.insert(SearchHistory(Device(model, csc), Date(year, month, day)))
        }

        cleanUpHistory()
    }

    fun update(model: String, csc: String, year: Int, month: Int, day: Int) =
        viewModelScope.launch {
            historyRepository.update(SearchHistory(Device(model, csc), Date(year, month, day)))
        }

    fun delete(model: String, csc: String) = viewModelScope.launch {
        historyRepository.delete(SearchHistory(Device(model, csc), Date(0, 0, 0)))
    }

    fun deleteAll() = viewModelScope.launch {
        historyRepository.deleteAll()
    }
}