package com.example.cs551fitnessapp.ui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ThemeViewModel : ViewModel() {

    var isDarkTheme = mutableStateOf(false)
        private set

    fun toggleTheme(value: Boolean) {
        isDarkTheme.value = value
    }
}