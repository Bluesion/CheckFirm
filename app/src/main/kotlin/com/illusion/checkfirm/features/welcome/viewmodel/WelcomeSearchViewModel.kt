package com.illusion.checkfirm.features.welcome.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.illusion.checkfirm.data.model.local.WelcomeSearchEntity
import com.illusion.checkfirm.data.repository.WelcomeSearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeSearchViewModel @Inject constructor(
    private val welcomeSearchRepository: WelcomeSearchRepository
) : ViewModel() {

    val allDevices: Flow<List<WelcomeSearchEntity>> = welcomeSearchRepository.allDevices

    fun insert(model: String, csc: String) = viewModelScope.launch(Dispatchers.IO) {
        welcomeSearchRepository.insert(WelcomeSearchEntity(null, model, csc, model + csc))
    }

    fun delete(device: String) = viewModelScope.launch(Dispatchers.IO) {
        welcomeSearchRepository.delete(device)
    }
}