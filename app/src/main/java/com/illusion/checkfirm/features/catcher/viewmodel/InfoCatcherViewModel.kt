package com.illusion.checkfirm.features.catcher.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.illusion.checkfirm.data.model.local.InfoCatcherEntity
import com.illusion.checkfirm.data.repository.InfoCatcherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class InfoCatcherViewModel(private val infoCatcherRepository: InfoCatcherRepository) : ViewModel() {

    val allDevices: Flow<List<InfoCatcherEntity>> = infoCatcherRepository.allDevices

    fun insert(model: String, csc: String) = viewModelScope.launch(Dispatchers.IO) {
        infoCatcherRepository.insert(InfoCatcherEntity(null, model, csc, model + csc))
    }

    fun delete(device: String) = viewModelScope.launch(Dispatchers.IO) {
        infoCatcherRepository.delete(device)
    }
}