package com.example.cs551fitnessapp.ui.viewmodels

import android.app.Application
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.cs551fitnessapp.database.DatabaseModule
import com.example.cs551fitnessapp.database.WorkoutEntry
import com.example.cs551fitnessapp.database.WorkoutPlanData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

// -- Save result  ---------------------------------------------------------------
sealed class SavePlanResult {
    data object Idle    : SavePlanResult()
    data object Loading : SavePlanResult()
    data class Success(val sessionId: Long) : SavePlanResult()
    data class Error(val message: String)   : SavePlanResult()
}

class WorkoutPlanViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DatabaseModule.provideRepository(application)

    // -- Save result state -------------------------------------------------------
    private val _saveResult = MutableStateFlow<SavePlanResult>(SavePlanResult.Idle)
    val saveResult: StateFlow<SavePlanResult> = _saveResult.asStateFlow()

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

    // -- Helper -------------------------------------------------------
    private fun todayFormatted(): String {
        val monthNames = listOf(
            "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        )
        val cal = Calendar.getInstance()
        return "%02d %s %d".format(
            cal.get(Calendar.DAY_OF_MONTH),
            monthNames[cal.get(Calendar.MONTH)],
            cal.get(Calendar.YEAR)
        )
    }
    
    // -- Save workout plan to db -------------------------------------------------------
    fun savePlan(plan: WorkoutPlanData) {
        viewModelScope.launch {
            _saveResult.value = SavePlanResult.Loading

            try {
//                val sessionId = repository.saveWorkoutPlan(
//                    userId = 1, // hardcoded for now
//                    plan   = plan
//                )
                when (val result = repository.saveWorkoutPlan(1, plan)) { // hardcoded for now

                    is SavePlanResult.Success ->
                        _saveResult.value = SavePlanResult.Success(result.sessionId)

                    is SavePlanResult.Error ->
                        _saveResult.value = SavePlanResult.Error(result.message)

                    else -> {}
                }

            } catch (e: Exception) {
                _saveResult.value = SavePlanResult.Error(
                    e.message ?: "Failed to save workout session"
                )
            }
        }
    }

    fun resetSaveResult() {
        _saveResult.value = SavePlanResult.Idle
    }

    fun clearAllValue() {
        _addedEntries.value = emptyList()
        _sessionName.value  = ""
        _selectedDate.value = todayFormatted()
        _startHour.value    = 0
        _startMin.value     = 0
        _endHour.value      = 0
        _endMin.value       = 0
        _saveResult.value   = SavePlanResult.Idle
    }

}
