package com.example.cs551fitnessapp.ui.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cs551fitnessapp.ui.screens.Workout
import com.example.cs551fitnessapp.ui.viewmodels.states.TodayUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import com.example.cs551fitnessapp.repository.WorkoutRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.time.ZoneId

//Temporary until database has these entities
data class MemberSession(
    val id: Long,
    val name: String, //Session name
    val dtSessionStart: String, // Start Session
    val dtSessionEnd: String,
    val status: String
)

@RequiresApi(Build.VERSION_CODES.O)
val workouts = listOf<Workout>(
    Workout (id = 0 , date = LocalDate.now().minusDays(-1) , start = "" , duration = "" , memberId = 0),
    Workout (id = 0 , date = LocalDate.now().minusDays(1) , start = "" , duration = "" , memberId = 1),
    Workout (id = 0 , date = LocalDate.now() , start = "" , duration = "" , memberId = 2),
    Workout (id = 0 , date = LocalDate.now() , start = "" , duration = "" , memberId = 3),
)


@RequiresApi(Build.VERSION_CODES.O)
class TodayViewModel(private val repository: WorkoutRepository) : ViewModel() {


    @RequiresApi(Build.VERSION_CODES.O)
    val currentDate = LocalDate.now()


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
        emptyList()))

    @RequiresApi(Build.VERSION_CODES.O)
    val uiState : StateFlow<TodayUiState> = _uiState.asStateFlow()

    init {
        loadMembers()
    }

    // Load members from repo
    private fun loadMembers() {
        viewModelScope.launch {
            val (start, end) = getTodayRange()
            repository.getalluserSessions(start, end)
                .catch { e ->
                    emit(emptyList())
                }
                .collect { MemberSession ->
                    _uiState.update { state ->
                        state.copy(members = MemberSession)
                    }
                }
        }
    }

    fun getTodayRange(): Pair<Long, Long> {
        val zoneId = ZoneId.systemDefault()
        val today = LocalDate.now()

        val startOfDay = today.atStartOfDay(zoneId).toInstant().toEpochMilli()
        val endOfDay = today.plusDays(1)
            .atStartOfDay(zoneId)
            .toInstant()
            .toEpochMilli() - 1

        return Pair(startOfDay, endOfDay)
    }

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