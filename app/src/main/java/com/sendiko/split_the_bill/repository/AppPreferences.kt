package com.sendiko.split_the_bill.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppPreferences(private val dataStore: DataStore<Preferences>){

    private val darkThemeKey = booleanPreferencesKey("is_dark")

    fun getDarkTheme(): Flow<Boolean> {
        return dataStore.data.map {
            it[darkThemeKey]?:false
        }
    }

    suspend fun setDarkTheme(isDarkTheme: Boolean){
        dataStore.edit {
            it[darkThemeKey] = isDarkTheme
        }
    }
}