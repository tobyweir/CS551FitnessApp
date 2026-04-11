package com.example.cs551fitnessapp.ui.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cs551fitnessapp.ui.viewmodels.states.TodayUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date


class TodayViewModel() : ViewModel() {

    @RequiresApi(Build.VERSION_CODES.O)
    val currentDate = LocalDate.now()

    // Convert Date to Local Date, may be needed later
    // dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    @RequiresApi(Build.VERSION_CODES.O)
    private val _uiState  = MutableStateFlow(TodayUiState(currentDate.minusDays(3),
        currentDate.minusDays(2),
        currentDate.minusDays(1),
        currentDate,
        currentDate.minusDays(-1),
        currentDate.minusDays(-2),
        currentDate.minusDays(-3),
        currentDate.dayOfMonth.toString(),
        listOf(),
        listOf()))
    @RequiresApi(Build.VERSION_CODES.O)
    val uiState : StateFlow<TodayUiState> = _uiState.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateSelectedDay (newDay : String) {
        _uiState.update { uiState ->
            uiState.copy(
                selectedDay = newDay
            )
        }
    }
}