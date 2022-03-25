package com.githubuserapps.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.githubuserapps.data.preference.SettingsPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ThemeViewModel(application: Application) : AndroidViewModel(application) {

    private val uiDataStore = SettingsPreferences(application)

    val getThemeMode = uiDataStore.themeMode

    fun saveToDataStore(isNightMode: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            uiDataStore.saveToDataStore(isNightMode)
        }
    }
}