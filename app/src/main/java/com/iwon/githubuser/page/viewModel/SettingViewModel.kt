package com.iwon.githubuser.page.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.iwon.githubuser.helper.SettingPreferences
import kotlinx.coroutines.launch

class SettingViewModel(private val preferences: SettingPreferences) : ViewModel() {

    fun getTheme(): LiveData<Boolean>{
        return preferences.getTheme().asLiveData()
    }

    fun setTheme(isDarkMode: Boolean){
        viewModelScope.launch {
            preferences.setTheme(isDarkMode)
        }
    }

}