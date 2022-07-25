package com.iwon.githubuser.helper

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingPreferences private constructor(private val dataStore: DataStore<Preferences>){

    private val THEME_KEY = booleanPreferencesKey("theme_setting")

    fun getTheme(): Flow<Boolean>{
        return dataStore.data.map { preference ->
            preference[THEME_KEY] ?: false
        }
    }

    suspend fun setTheme(isDarkMode: Boolean){
        dataStore.edit { preference ->
            preference[THEME_KEY] = isDarkMode
        }
    }

    companion object{
        @Volatile
        private var INSTANCE : SettingPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>) : SettingPreferences{
            return INSTANCE ?: synchronized(this){
                val instance = SettingPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

}