package com.example.myaku_rismu.core.base

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyakuRismuApplication : Application() {
    val dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")
}