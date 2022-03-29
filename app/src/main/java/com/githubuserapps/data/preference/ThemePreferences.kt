package com.githubuserapps.data.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.githubuserapps.util.Constant.Companion.DATASTORE_PREFERENCE
import com.githubuserapps.util.Constant.Companion.THEME_MODE_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(DATASTORE_PREFERENCE)

class SettingsPreferences(context: Context) {

    companion object {
        private val THEME_KEY = booleanPreferencesKey(THEME_MODE_KEY)
    }

    private val dataStore: DataStore<Preferences> = context.dataStore

    suspend fun setThemeMode(isNightMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = isNightMode
        }
    }

    fun getThemeMode(): Flow<Boolean> = dataStore.data.map { preferences ->
        val themeMode = preferences[THEME_KEY] ?: false
        themeMode
    }

}