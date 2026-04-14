package com.example.cs551fitnessapp.database

import android.content.Context

import androidx.datastore.core.DataStore

import androidx.datastore.preferences.core.Preferences as DataStorePreferences

import androidx.datastore.preferences.core.booleanPreferencesKey

import androidx.datastore.preferences.preferencesDataStore

import com.example.cs551fitnessapp.repository.NotificationSettingsRepository



val Context.notificationDataStore: DataStore<DataStorePreferences>

        by preferencesDataStore(

            name = "notification_settings"

        )



object NotificationPreferencesKeys {


    val PERIODIC_NOTIFICATION_ENABLED =

        booleanPreferencesKey(

            "periodic_notification_enabled"

        )



    val DYNAMIC_NOTIFICATION_ENABLED =

        booleanPreferencesKey(

            "dynamic_notification_enabled"

        )



    val DARK_THEME_ENABLED =

        booleanPreferencesKey(

            "dark_theme_enabled"

        )

}



/**

 * Extension to provide the repository instance.

 * Used in SettingsScreen.

 */

fun Context.provideRepository(): NotificationSettingsRepository {


    return NotificationSettingsRepository(

        notificationDataStore

    )

}