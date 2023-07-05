package com.alvaro.suitemdiamd.data.pagging

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SetPreference private constructor(private val dataStrore: DataStore<Preferences>) {

    private val INSERT_USER_NAME = stringPreferencesKey("username")
    private val SELECTED_USER_NAME = stringPreferencesKey("selected_username")

    fun getSelectedUsername(): Flow<Users> {
        return dataStrore.data.map { pref ->
            Users(
                pref[INSERT_USER_NAME] ?: "", pref[SELECTED_USER_NAME] ?: "Choose a User Name"
            )
        }
    }

    suspend fun saveSelected(selected: String) {
        dataStrore.edit { pref -> pref[SELECTED_USER_NAME] = selected }
    }

    suspend fun saveName(name: String) {
        dataStrore.edit { pref -> pref[INSERT_USER_NAME] = name }
    }

    companion object {
        @Volatile
        private var PREFERENCE: SetPreference? = null

        fun getInstance(dataStore: DataStore<Preferences>): SetPreference {
            return PREFERENCE ?: synchronized(this) {
                val instance = SetPreference(dataStore)
                PREFERENCE = instance
                instance
            }
        }
    }
}