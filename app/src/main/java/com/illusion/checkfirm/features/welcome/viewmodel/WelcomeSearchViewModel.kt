package com.illusion.checkfirm.features.welcome.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.illusion.checkfirm.data.model.WelcomeSearchEntity
import com.illusion.checkfirm.data.repository.WelcomeSearchRepository
import com.illusion.checkfirm.data.source.local.WelcomeSearchDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class WelcomeSearchViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: WelcomeSearchRepository
    val allDevices: Flow<List<WelcomeSearchEntity>>

    init {
        val welcomeSearchDao = WelcomeSearchDatabase.getDatabase(application).welcomeSearchDao()
        repository = WelcomeSearchRepository(welcomeSearchDao)
        allDevices = repository.allDevices
    }

    fun insert(model: String, csc: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(WelcomeSearchEntity(null, model, csc, model + csc))
    }

    fun delete(device: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(device)
    }
}