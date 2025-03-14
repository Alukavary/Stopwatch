package com.example.stopwatch

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("data_store")

class DataStoreManager(val context: Context) {

    suspend fun saveIsRun(isRun: Boolean) {
        context.dataStore.edit { data ->
            data[booleanPreferencesKey("is_run")] = isRun

        }
    }

    suspend fun saveIsVisible(isVisible: Boolean) {
        context.dataStore.edit { data ->
            data[booleanPreferencesKey("is_visible")] = isVisible
        }
    }

    suspend fun saveColor(color: Boolean) {
        context.dataStore.edit { data ->
            data[booleanPreferencesKey("is_green")] = color
        }
    }

    suspend fun saveBtn1(btn1: String) {
        context.dataStore.edit { data ->
            data[stringPreferencesKey("btnState1")] = btn1
        }
    }

    suspend fun saveBtnState2(btn2: String) {
        context.dataStore.edit { data ->
            data[stringPreferencesKey("btnState2")] = btn2
        }
    }

    suspend fun saveTimer(hours: Int, min: Int, sec: Int) {
        context.dataStore.edit { data ->
            data[intPreferencesKey("hours")] = hours
            data[intPreferencesKey("min")] = min
            data[intPreferencesKey("sec")] = sec
        }
    }


    suspend fun saveLapCount(count: Int) {
        context.dataStore.edit { data ->
            data[intPreferencesKey("lap_count")] = count
        }
    }

    suspend fun saveItemData(itemData: String) {
        context.dataStore.edit { data ->
            data[stringPreferencesKey("item_data")] = itemData
        }
    }


    fun getSettings() = context.dataStore.data.map { pref ->
        return@map SettingsDataClass(
            isRun = pref[booleanPreferencesKey("is_run")] ?: true,
            isVisible = pref[booleanPreferencesKey("is_visible")] ?: false,
            isGreenOr = pref[booleanPreferencesKey("is_green")] ?: true,
            button1StateStartStop = pref[stringPreferencesKey("btnState1")] ?: "Start",
            button2StateResetCircle = pref[stringPreferencesKey("btnState2")] ?: "Lap",
            timerHours = pref[intPreferencesKey("hours")] ?: 0,
            timerMin = pref[intPreferencesKey("min")] ?: 0,
            timerSec = pref[intPreferencesKey("sec")] ?: 0,
            lapCount = pref[intPreferencesKey("lap_count")] ?: 0

        )
    }
}