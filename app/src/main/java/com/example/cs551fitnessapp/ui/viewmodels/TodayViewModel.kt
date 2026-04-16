package com.example.cs551fitnessapp.ui.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cs551fitnessapp.ui.screens.TodayMember
import com.example.cs551fitnessapp.ui.screens.Workout
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

val members = listOf(
    TodayMember(id = 0, name = "John Smith"),
    TodayMember(id = 1, name = "Mike Smith"),
    TodayMember(id = 2, name = "Major Smith"),
    TodayMember(id = 3, name = "Jill Smith"),
    TodayMember(id = 4, name = "Mike Smith"),
    TodayMember(id = 5, name = "Major Smith"),
    TodayMember(id = 6, name = "John Smith"),
    TodayMember(id = 7, name = "Mike Smith"),
    TodayMember(id = 8, name = "Major Smith")
)

@RequiresApi(Build.VERSION_CODES.O)
val workouts = listOf<Workout>(
    Workout (id = 0 , date = LocalDate.now().minusDays(-1) , start = "" , duration = "" , memberId = 0),
    Workout (id = 0 , date = LocalDate.now().minusDays(1) , start = "" , duration = "" , memberId = 1),
    Workout (id = 0 , date = LocalDate.now() , start = "" , duration = "" , memberId = 2),
    Workout (id = 0 , date = LocalDate.now() , start = "" , duration = "" , memberId = 3),
)


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
        currentDate,
        workouts,
        workouts.filter { it.date == currentDate },
        members))
    @RequiresApi(Build.VERSION_CODES.O)
    val uiState : StateFlow<TodayUiState> = _uiState.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateSelectedDay (newDay : LocalDate) {
        _uiState.update { uiState ->
            uiState.copy(
                selectedDay = newDay
            )
        }
        filterWorkouts()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun filterWorkouts() {
        val newWorkouts = _uiState.value.workouts.filter {it.date == _uiState.value.selectedDay }
        _uiState.update { uiState ->
            uiState.copy(
                 filteredWorkouts = newWorkouts
            )
        }
    }
}