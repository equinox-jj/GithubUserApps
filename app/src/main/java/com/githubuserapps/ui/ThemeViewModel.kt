package com.githubuserapps.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.githubuserapps.data.preference.SettingsPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ThemeViewModel(application: Application) : AndroidViewModel(application) {

    private val uiDataStore = SettingsPreferences(application)

    fun themeState() = uiDataStore.getThemeMode()

    fun getThemeMode() = uiDataStore.getThemeMode().asLiveData()

    fun setThemeMode(isNightMode: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            uiDataStore.setThemeMode(isNightMode)
        }
    }
}