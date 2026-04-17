package com.example.cs551fitnessapp.ui.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cs551fitnessapp.repository.WorkoutRepository
import com.example.cs551fitnessapp.ui.screens.Workout
import com.example.cs551fitnessapp.ui.viewmodels.states.TodayUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId

data class MemberSession(
    val id: Long,
    val name: String,
    val dtSessionStart: String,
    val dtSessionEnd: String,
    val status: String
)

@RequiresApi(Build.VERSION_CODES.O)
private val workouts = listOf(
    Workout(id = 0, date = LocalDate.now().minusDays(-1), start = "", duration = "", memberId = 0),
    Workout(id = 0, date = LocalDate.now().minusDays(1), start = "", duration = "", memberId = 1),
    Workout(id = 0, date = LocalDate.now(), start = "", duration = "", memberId = 2),
    Workout(id = 0, date = LocalDate.now(), start = "", duration = "", memberId = 3)
)

@RequiresApi(Build.VERSION_CODES.O)
class TodayViewModel(private val repository: WorkoutRepository) : ViewModel() {

    val currentDate = LocalDate.now()

    private val _uiState = MutableStateFlow(
        TodayUiState(
            day1 = currentDate.minusDays(3),
            day2 = currentDate.minusDays(2),
            day3 = currentDate.minusDays(1),
            day4 = currentDate,
            day5 = currentDate.minusDays(-1),
            day6 = currentDate.minusDays(-2),
            day7 = currentDate.minusDays(-3),
            selectedDay = currentDate,
            workouts = workouts,
            filteredWorkouts = workouts.filter { it.date == currentDate },
            members = emptyList()
        )
    )

    val uiState: StateFlow<TodayUiState> = _uiState.asStateFlow()

    init {
        loadSessionsForSelectedDay()
    }

    private fun loadSessionsForSelectedDay() {
        viewModelScope.launch {
            val (start, end) = getDayRange(_uiState.value.selectedDay)
            repository.getAllSessionsInRange(start, end)
                .catch {
                    emit(emptyList())
                }
                .collect { memberSessions ->
                    _uiState.update { state ->
                        state.copy(members = memberSessions)
                    }
                }
        }
    }

    fun getTodayRange(): Pair<Long, Long> {
        return getDayRange(LocalDate.now())
    }

    private fun getDayRange(day: LocalDate): Pair<Long, Long> {
        val zoneId = ZoneId.systemDefault()

        val startOfDay = day.atStartOfDay(zoneId).toInstant().toEpochMilli()
        val endOfDay = day.plusDays(1)
            .atStartOfDay(zoneId)
            .toInstant()
            .toEpochMilli() - 1

        return Pair(startOfDay, endOfDay)
    }

    fun updateSelectedDay(newDay: LocalDate) {
        _uiState.update { uiState ->
            uiState.copy(selectedDay = newDay)
        }
        filterWorkouts()
        loadSessionsForSelectedDay()
    }

    fun filterWorkouts() {
        val newWorkouts = _uiState.value.workouts.filter { it.date == _uiState.value.selectedDay }
        _uiState.update { uiState ->
            uiState.copy(filteredWorkouts = newWorkouts)
        }
    }
}