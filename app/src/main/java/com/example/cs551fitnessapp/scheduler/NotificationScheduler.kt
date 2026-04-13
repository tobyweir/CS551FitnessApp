package com.example.cs551fitnessapp.scheduler

import android.content.Context
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.cs551fitnessapp.database.SessionEntity
import com.example.cs551fitnessapp.worker.ReminderWorker
import java.util.concurrent.TimeUnit

class NotificationScheduler(context: Context) {

    private val wm = WorkManager.getInstance(context)

    fun schedule(intervalHours: Long, inputData: Data? = null) {

        val request =
            PeriodicWorkRequestBuilder<ReminderWorker>(intervalHours, TimeUnit.HOURS) //For debugging
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                        .build()
                )
                .setInitialDelay(intervalHours, TimeUnit.HOURS)
                .setInputData(inputData ?: Data.EMPTY)
                .addTag(ReminderWorker.WORK_TAG_PERIODIC)
                .build()

        wm.enqueueUniquePeriodicWork(
            ReminderWorker.WORK_TAG_PERIODIC,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    fun scheduleEventReminder(context: Context, event: SessionEntity) {
        val now = System.currentTimeMillis()
        val triggerTime = event.dtStartSession - (30 * 60 * 1000) //remind earier 30 mins before session start
        val delay = triggerTime - System.currentTimeMillis()


        if (delay <= 0) return

        val request = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(
                workDataOf(
                    "type" to "event",
                    "title" to "Upcoming Session",
                    "msg" to event.ownerUserId.toString() +" session is about to start in 30 mins",
                    "eventTimeMillis" to event.dtStartSession
                )
            )
            .addTag(ReminderWorker.WORK_TAG_DYNAMIC)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "event_${event.sessionId}",
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    /** Cancel all pending periodic reminders. */
    fun cancelPeriodicNoti() = wm.cancelUniqueWork(ReminderWorker.WORK_TAG_PERIODIC)

    /** Cancel all pending upcoming session reminders. */
    fun cancelDynamicNoti() = wm.cancelAllWorkByTag(ReminderWorker.WORK_TAG_DYNAMIC)

    /** Fire a one-shot notification immediately. */
    fun fireNow(inputData: Data? = null) {
        val request = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInputData(inputData ?: Data.EMPTY)
            .addTag("Testing")
            .build()
        wm.enqueue(request)
    }
}