package com.illusion.checkfirm.database.catcher

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class InfoCatcherViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: InfoCatcherRepository
    val allDevices: LiveData<List<InfoCatcherEntity>>

    init {
        val catcherDao = InfoCatcherDatabase.getDatabase(application, viewModelScope).catcherDao()
        repository = InfoCatcherRepository(catcherDao)
        allDevices = repository.allDevices
    }

    fun insert(model: String, csc: String) = viewModelScope.launch {
        InsertThread(model, csc).start()
    }

    fun delete(device: String) = viewModelScope.launch {
        DeleteThread(device).start()
    }

    inner class InsertThread(val model: String, val csc: String) : Thread() {
        override fun run() {
            val entity = InfoCatcherEntity(null, model, csc, model + csc)
            repository.insert(entity)
        }
    }

    inner class DeleteThread(val device: String) : Thread() {
        override fun run() {
            repository.delete(device)
        }
    }
}