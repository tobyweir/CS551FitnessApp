package com.example.cs551fitnessapp.ui.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cs551fitnessapp.repository.WorkoutRepository
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
    val sessionId: Long,
    val memberId: Long,
    val memberName: String,
    val sessionName: String,
    val startTime: String,
    val duration: String,
    val endTime: String
)

@RequiresApi(Build.VERSION_CODES.O)
class TodayViewModel(private val repository: WorkoutRepository) : ViewModel() {

    private val currentDate = LocalDate.now()

    private val _uiState = MutableStateFlow(
        TodayUiState(
            day1 = currentDate.minusDays(3),
            day2 = currentDate.minusDays(2),
            day3 = currentDate.minusDays(1),
            day4 = currentDate,
            day5 = currentDate.plusDays(1),
            day6 = currentDate.plusDays(2),
            day7 = currentDate.plusDays(3),
            selectedDay = currentDate,
            sessions = emptyList()
        )
    )

    val uiState: StateFlow<TodayUiState> = _uiState.asStateFlow()

    init {
        loadSessionsForSelectedDay()
    }

    fun updateSelectedDay(newDay: LocalDate) {
        _uiState.update { it.copy(selectedDay = newDay) }
        loadSessionsForSelectedDay()
    }

    private fun loadSessionsForSelectedDay() {
        viewModelScope.launch {
            val (start, end) = getDayRange(_uiState.value.selectedDay)
            repository.getAllSessionsInRange(start, end)
                .catch {
                    emit(emptyList())
                }
                .collect { sessions ->
                    _uiState.update { state ->
                        state.copy(sessions = sessions)
                    }
                }
        }
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
}