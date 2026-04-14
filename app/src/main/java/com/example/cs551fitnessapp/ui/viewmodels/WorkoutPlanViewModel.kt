package com.example.cs551fitnessapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.cs551fitnessapp.database.WorkoutEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Calendar

class WorkoutPlanViewModel : ViewModel() {

    // -- Workout entries -------------------------------------------------------
    private val _addedEntries = MutableStateFlow<List<WorkoutEntry>>(emptyList())
    val addedEntries: StateFlow<List<WorkoutEntry>> = _addedEntries.asStateFlow()

    fun addEntry(entry: WorkoutEntry) {
        _addedEntries.value = _addedEntries.value + entry
    }

    fun removeEntry(exerciseId: String) {
        _addedEntries.value = _addedEntries.value.filter { it.exercise.id != exerciseId }
    }

    fun isAdded(exerciseId: String): Boolean =
        _addedEntries.value.any { it.exercise.id == exerciseId }

    // -- Form fields -------------------------------------------------------

    private val _sessionName  = MutableStateFlow("Session")

    private val _selectedDate = MutableStateFlow(todayFormatted())

    // Spinners store Int — default 0
    private val _startHour = MutableStateFlow(0)
    private val _startMin  = MutableStateFlow(0)
    private val _endHour   = MutableStateFlow(0)
    private val _endMin    = MutableStateFlow(0)

    val sessionName  : StateFlow<String> = _sessionName.asStateFlow()
    val selectedDate : StateFlow<String> = _selectedDate.asStateFlow()
    val startHour    : StateFlow<Int>    = _startHour.asStateFlow()
    val startMin     : StateFlow<Int>    = _startMin.asStateFlow()
    val endHour      : StateFlow<Int>    = _endHour.asStateFlow()
    val endMin       : StateFlow<Int>    = _endMin.asStateFlow()

    fun onSessionNameChange(value: String)  { _sessionName.value  = value }
    fun onSelectedDateChange(value: String) { _selectedDate.value = value }
    fun onStartHourChange(value: Int)       { _startHour.value    = value }
    fun onStartMinChange(value: Int)        { _startMin.value     = value }
    fun onEndHourChange(value: Int)         { _endHour.value      = value }
    fun onEndMinChange(value: Int)          { _endMin.value       = value }

    // ── Helper ─────────────────────────────────────────────────────────────
    private fun todayFormatted(): String {
        val monthNames = listOf(
            "Jan","Feb","Mar","Apr","May","Jun",
            "Jul","Aug","Sep","Oct","Nov","Dec"
        )
        val cal = Calendar.getInstance()
        return "%02d %s %d".format(
            cal.get(Calendar.DAY_OF_MONTH),
            monthNames[cal.get(Calendar.MONTH)],
            cal.get(Calendar.YEAR)
        )
    }
}