package com.illusion.checkfirm.features.search.viewmodel

import androidx.lifecycle.ViewModel
import com.illusion.checkfirm.common.util.Tools
import com.illusion.checkfirm.data.model.local.DeviceItem
import com.illusion.checkfirm.data.model.local.SearchDeviceItem
import com.illusion.checkfirm.features.search.util.SearchValidationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor() : ViewModel() {

    private val _searchList = MutableStateFlow<List<SearchDeviceItem>>(emptyList())
    val searchList = _searchList.asStateFlow()

    fun onItemClicked(device: DeviceItem): SearchValidationResult {
        val validationResult = addToSearchList(device)
        if (validationResult == SearchValidationResult.DUPLICATED_DEVICE) {
            for (searchListDevice in _searchList.value) {
                if (searchListDevice.device == device) {
                    removeFromSearchList(device)
                }
            }
            return SearchValidationResult.SUCCESS
        }

        return validationResult
    }

    fun addToSearchList(model: String, csc: String): SearchValidationResult {
        return addToSearchList(
            DeviceItem(
                model.trim().uppercase(Locale.US),
                csc.trim().uppercase(Locale.US)
            )
        )
    }

    /**
     * Adds a device to the searchList if it is valid and not already present.
     *
     * @param device The device to be added.
     * @return [SearchValidationResult] indicating the result of the operation.
     */
    fun addToSearchList(device: DeviceItem): SearchValidationResult {
        if (_searchList.value.isNotEmpty()) {
            for (i in _searchList.value.indices) {
                if (device == _searchList.value[i].device) {
                    return SearchValidationResult.DUPLICATED_DEVICE
                }
            }
        }

        if (!Tools.isValidDevice(device)) {
            return SearchValidationResult.INVALID_DEVICE
        }

        if (MAX_SEARCH_CAPACITY - _searchList.value.size == 0) {
            return SearchValidationResult.MAX_SEARCH_CAPACITY_EXCEEDED
        }

        val currentList = _searchList.value.toMutableList()
        currentList.add(SearchDeviceItem(device))
        _searchList.value = currentList

        return SearchValidationResult.SUCCESS
    }

    fun removeFromSearchList(device: DeviceItem) {
        _searchList.value.forEachIndexed { index, searchDevice ->
            if (searchDevice.device == device) {
                removeFromSearchList(index)
            }
        }
    }

    /**
     * Adds a device to the search list if it is valid and not already present.
     *
     * @param position The position in [searchList] to remove the device from.
     * @return Positive integer (including 0) which means remain capacity if removed successfully.
     */
    fun removeFromSearchList(position: Int) {
        val currentList = _searchList.value.toMutableList()
        currentList.removeAt(position)
        _searchList.value = currentList
    }

    companion object {
        const val MAX_SEARCH_CAPACITY = 5
    }
}