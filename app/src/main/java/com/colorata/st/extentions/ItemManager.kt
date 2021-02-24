package com.colorata.st.extentions

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.createDataStore
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.createDataStore
import java.util.prefs.Preferences

class ItemManager(context: Context) {
    private val datastore = context.createDataStore(name = "user_prefs")
    companion object{
        val NAMES = stringSetPreferencesKey("names")
        val ICONS = stringSetPreferencesKey("icons")
    }
}