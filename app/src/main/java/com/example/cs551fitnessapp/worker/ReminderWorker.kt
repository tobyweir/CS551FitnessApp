package com.example.cs551fitnessapp.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.cs551fitnessapp.R
import java.util.concurrent.TimeUnit
import com.example.cs551fitnessapp.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class ReminderWorker(private val context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {

        val type = inputData.getString("type") ?: "notification type"
        val title = inputData.getString("title") ?: "Reminder"
        var msg = inputData.getString("msg") ?: "notification msg"

        if (type == "periodic") {
            val sessionDao = AppDatabase.getDatabase(applicationContext).sessionDao()
            val sevenDaysAgo = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(7)
            val sessions = sessionDao.getAllSessionsInRange(sevenDaysAgo, System.currentTimeMillis()).first()
            val totalHourTraining = (sessions.sumOf { it.duration.toDouble() })/60 //convert minute to hr
            msg = "Weekly training: %.2f hours".format(totalHourTraining)

        }

        if (type == "event") {
            val eventTime = inputData.getLong("eventTimeMillis", 0L)
            if (!ontimeNotifyCheck(eventTime)) {
                return@withContext Result.success()
            }
        }

        try {
            showNotification(title, msg, type)
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext Result.retry()
        }

        return@withContext Result.success()
    }

    companion object {
        const val WORK_TAG_PERIODIC = "periodic_reminder_work"
        const val WORK_TAG_DYNAMIC = "dynamic_reminder_work"
    }

    private fun showNotification(title: String, content: String, type: String) {
        val channelId = "periodic_channel"
        val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = context.packageManager
            .getLaunchIntentForPackage(context.packageName)?.apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = manager.getNotificationChannel(channelId) ?: NotificationChannel(
                channelId,
                "Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Reminders for fitness tracking"
            }
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setLargeIcon(BitmapFactory.decodeResource(applicationContext.resources, if(type == "event") R.drawable.dumbbell_blue else R.drawable.hourglass))
            .build()

        manager.notify(1001, notification)
    }

    private fun ontimeNotifyCheck(eventTime: Long): Boolean {
        val now = System.currentTimeMillis()
        val diff = eventTime - now
        return diff in 0..(30 * 60 * 1000)
    }
}