package com.example.cs551fitnessapp.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.work.workDataOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import androidx.lifecycle.AndroidViewModel
import androidx.room.Room
import com.example.cs551fitnessapp.repository.EventRepository
import com.example.cs551fitnessapp.database.Database
import com.example.cs551fitnessapp.database.SessionDao
import com.example.cs551fitnessapp.database.UserAppointmentDao
import com.example.cs551fitnessapp.database.UserAppointmentEntity
import com.example.cs551fitnessapp.database.SessionEntity
import com.example.cs551fitnessapp.repository.NotificationSettings
import com.example.cs551fitnessapp.repository.NotificationSettingsRepository
import com.example.cs551fitnessapp.scheduler.NotificationScheduler
import com.example.cs551fitnessapp.ui.viewmodels.states.NotificationSettingsUiState
import kotlinx.coroutines.flow.*
import java.util.concurrent.TimeUnit


class NotificationSettingsViewModel(
    application: Application,
    private val repository: NotificationSettingsRepository,
    private val scheduler: NotificationScheduler,
    private val sessionDao: SessionDao
) : AndroidViewModel(application) {

    private val context = getApplication<Application>()

    private val eventsFlow = sessionDao.getAllSessionsInRange(0L, Long.MAX_VALUE)

    val uiState: StateFlow<NotificationSettingsUiState> =
        repository.notificationSettings
            .toUiState()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = NotificationSettingsUiState(isLoading = true)
            )

    fun onNotiPeriodicToggled(enabled: Boolean) {
        viewModelScope.launch {
            repository.setPeriodicNotificationEnabled(enabled)
            if (enabled) {

                val data = workDataOf(
                    "type" to "periodic",
                    "title" to "Weekly Report"
                )
                // Schedule periodic work
                scheduler.schedule(7, data) // Schedule every 7 days
                // Trigger an immediate notification for debugging
               scheduler.fireNow(data)

            } else {
                scheduler.cancelPeriodicNoti()
            }
        }
    }

    init {
        observeDynamicReminders()
    }

    private fun observeDynamicReminders() {
        viewModelScope.launch {
            combine(
                repository.notificationSettings.map { it.isDynamicEnabled },
                eventsFlow
            ) { enabled: Boolean, events: List<SessionEntity> ->
                Pair(enabled, events)
            }.collect { (enabled, events) ->
                if (enabled) {
                    events.forEach {
                        scheduler.scheduleEventReminder(context, it)
                    }
                }
                else {
                    scheduler.cancelDynamicNoti()
                }
            }
        }
    }

    fun onNotiDynamicToggled(enabled: Boolean) {
        viewModelScope.launch {
            repository.setDynamicNotificationEnabled(enabled)
        }
    }

    private fun Flow<NotificationSettings>.toUiState(): Flow<NotificationSettingsUiState> =
        map { settings ->
            NotificationSettingsUiState(
                isLoading = false,
                isPeriodicEnabled = settings.isPeriodicEnabled,
                isDynamicEnabled = settings.isDynamicEnabled
            )
        }

    companion object {
        fun provideFactory(
            application: Application,
            repository: NotificationSettingsRepository,
            scheduler: NotificationScheduler,
            sessionDao: SessionDao
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(NotificationSettingsViewModel::class.java)) {
                    return NotificationSettingsViewModel(application, repository, scheduler, sessionDao) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}