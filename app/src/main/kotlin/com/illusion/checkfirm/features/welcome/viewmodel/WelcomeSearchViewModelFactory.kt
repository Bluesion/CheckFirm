package com.illusion.checkfirm.features.welcome.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.illusion.checkfirm.data.repository.WelcomeSearchRepository

class WelcomeSearchViewModelFactory(private val welcomeSearchRepository: WelcomeSearchRepository) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WelcomeSearchViewModel::class.java)) {
            return WelcomeSearchViewModel(welcomeSearchRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}