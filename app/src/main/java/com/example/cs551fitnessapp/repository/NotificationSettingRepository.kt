package com.example.cs551fitnessapp.repository

import androidx.datastore.core.DataStore

import androidx.datastore.preferences.core.Preferences

import androidx.datastore.preferences.core.edit

import com.example.cs551fitnessapp.database.NotificationPreferencesKeys


import kotlinx.coroutines.flow.Flow

import kotlinx.coroutines.flow.map



data class NotificationSettings(

    val isPeriodicEnabled: Boolean = false,

    val isDynamicEnabled: Boolean = false

)



class NotificationSettingsRepository(

    private val dataStore: DataStore<Preferences>

) {



    /** Emits the latest NotificationSettings whenever DataStore changes */

    val notificationSettings: Flow<NotificationSettings> =

        dataStore.data.map { prefs ->

            NotificationSettings(

                isPeriodicEnabled =

                    prefs[NotificationPreferencesKeys.PERIODIC_NOTIFICATION_ENABLED]

                        ?: false,


                isDynamicEnabled =

                    prefs[NotificationPreferencesKeys.DYNAMIC_NOTIFICATION_ENABLED]

                        ?: false

            )

        }



    /** Dark theme preference */

    val isDarkThemeEnabled: Flow<Boolean> =

        dataStore.data.map { prefs ->

            prefs[NotificationPreferencesKeys.DARK_THEME_ENABLED]

                ?: false

        }



    suspend fun setPeriodicNotificationEnabled(enabled: Boolean) {

        dataStore.edit { prefs ->

            prefs[NotificationPreferencesKeys.PERIODIC_NOTIFICATION_ENABLED] = enabled

        }

    }



    suspend fun setDynamicNotificationEnabled(enabled: Boolean) {

        dataStore.edit { prefs ->

            prefs[NotificationPreferencesKeys.DYNAMIC_NOTIFICATION_ENABLED] = enabled

        }

    }



    suspend fun setDarkThemeEnabled(enabled: Boolean) {

        dataStore.edit { prefs ->

            prefs[NotificationPreferencesKeys.DARK_THEME_ENABLED] = enabled

        }

    }

}