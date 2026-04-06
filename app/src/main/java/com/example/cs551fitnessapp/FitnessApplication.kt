package com.example.cs551fitnessapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

class FitnessApplication : Application() {
    //lateinit var appContainer:AppContainer

    /*fun createNotificationChannel (context: Context){
        val channel = NotificationChannel(NotificationChannels.REMINDERS,"reminders", NotificationManager.IMPORTANCE_DEFAULT)
        //register the notification channel
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

    }

    override fun onCreate(){
        super.onCreate()
        appContainer = AppContainer(this)
        createNotificationChannel(this)

    }
    */
}