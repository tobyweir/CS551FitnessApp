package com.example.cs551fitnessapp.ui.viewmodels.states

data class NotificationSettingsUiState(
    val isLoading: Boolean = true,
    val isPeriodicEnabled: Boolean = false,
    val isDynamicEnabled: Boolean = false
)