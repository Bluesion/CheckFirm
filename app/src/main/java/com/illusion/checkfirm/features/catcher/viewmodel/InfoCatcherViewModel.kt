package com.illusion.checkfirm.features.catcher.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.illusion.checkfirm.data.repository.InfoCatcherRepository
import com.illusion.checkfirm.data.model.InfoCatcherEntity
import com.illusion.checkfirm.data.source.local.InfoCatcherDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class InfoCatcherViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: InfoCatcherRepository
    val allDevices: Flow<List<InfoCatcherEntity>>

    init {
        val catcherDao = InfoCatcherDatabase.getDatabase(application).catcherDao()
        repository = InfoCatcherRepository(catcherDao)
        allDevices = repository.allDevices
    }

    fun insert(model: String, csc: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(InfoCatcherEntity(null, model, csc, model + csc))
    }

    fun delete(device: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(device)
    }
}