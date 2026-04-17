package com.example.cs551fitnessapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.cs551fitnessapp.database.DatabaseModule
import com.example.cs551fitnessapp.database.member.MemberEntity
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.cs551fitnessapp.ui.viewmodels.states.AddMemberUiState
import com.example.cs551fitnessapp.ui.viewmodels.states.EditMemberUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AddMemberViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DatabaseModule.provideMemberRepository(application)
    private val _uiState  = MutableStateFlow(AddMemberUiState(
        isSexError = false,
        enableSexNext = false,
        isBirthdayError = false,
        isWeightError = false,
        isHeightError = false,
        isGoalError = false,
        isMedicalConcernError = false,
        isNameError = false,
        isSessionError = false,
        enableBirthdayNext = false,
        enableWeightNext = false,
        enableHeightNext = false,
        enableGoalNext = true,
        enableMedicalNext = true,
        enableSave = false
    ))
    val uiState : StateFlow<AddMemberUiState> = _uiState.asStateFlow()
    var sex by mutableStateOf("")
        private set

    var birthday by mutableStateOf("")
        private set

    var weight by mutableStateOf("")
        private set

    var weightUnit by mutableStateOf("lbs")
        private set

    var height by mutableStateOf("")
        private set

    var heightUnit by mutableStateOf("cm")
        private set

    var goal by mutableStateOf("")
        private set

    var goalId by mutableStateOf(0)
        private set

    var medicalConcern by mutableStateOf("")
        private set

    var medicalConcernId by mutableStateOf(0)
        private set

    var medicalNote by mutableStateOf("")
        private set

    var name by mutableStateOf("")
        private set

    var sessions by mutableStateOf(0)
        private set

    var sessionsInput by mutableStateOf("20")
        private set

    fun updateSex(value: String) {
        sex = value
        _uiState.update { uiState ->
            uiState.copy(
                enableSexNext = true
            )
        }
    }

    fun updateBirthday(value: String) {
        birthday = value
        if (birthday != "") {
            _uiState.update { uiState ->
                uiState.copy(
                    enableBirthdayNext = true
                )
            }
        } else {
            _uiState.update { uiState ->
                uiState.copy(
                    enableBirthdayNext = false
                )
            }
        }
    }

    fun updateWeight(value: String) {
        weight = value
            if (weight.toFloatOrNull() != null && weight.toFloatOrNull()!! > 0.0 ) {
                _uiState.update { uiState ->
                    uiState.copy(
                        enableWeightNext = true,
                        isWeightError = false
                    )
                }
            } else {
                _uiState.update { uiState ->
                    uiState.copy(
                        enableWeightNext = false,
                        isWeightError = true
                    )
                }
            }

    }

        fun updateWeightUnit(value: String) {
            weightUnit = value
        }

        fun updateHeight(value: String) {
            height = value
            if (height.toFloatOrNull() != null && height.toFloatOrNull()!! > 0.0) {
                _uiState.update { uiState ->
                    uiState.copy(
                        enableHeightNext = true,
                        isHeightError = false
                    )
                }
            } else {
                _uiState.update { uiState ->
                    uiState.copy(
                        enableHeightNext = false,
                        isHeightError = true
                    )
                }
            }
        }

        fun updateHeightUnit(value: String) {
            heightUnit = value
        }

        fun updateGoal(value: String) {
            goal = value
        }

        fun updateGoalSelection(id: Int) {
            goalId = id
            goal = when (id) {
                0 -> "Lose Weight"
                1 -> "Build Muscle"
                2 -> "Get Fit"
                else -> "General Fitness"
            }
        }

        fun updateMedicalConcern(value: String) {
            medicalConcern = value
        }

        fun updateMedicalConcernSelection(id: Int) {
            medicalConcernId = id
            medicalConcern = when (id) {
                0 -> "None"
                1 -> "Heart Condition"
                2 -> "Injury"
                3 -> "Mobility Concern"
                else -> "None"
            }
        }

        fun updateMedicalNote(value: String) {
            medicalNote = value
        }

        fun updateName(value: String) {
            name = value
            if (name != "") {
                _uiState.update { uiState ->
                    uiState.copy(
                        isNameError = false
                    )
                }
                if (sessionsInput.toIntOrNull() != null && (sessionsInput.toIntOrNull() ?: -1) > 0) {
                    _uiState.update { uiState ->
                        uiState.copy(
                            enableSave = true
                        )
                    }
                }
            } else {
                _uiState.update { uiState ->
                    uiState.copy(
                        isNameError = true,
                        enableSave = false
                    )
                }
            }
        }

        fun updateSessions(value: Int) {
            sessions = value
        }

        fun updateSessionsInput(value: String) {
            sessionsInput = value
            if (sessionsInput.toIntOrNull() != null && (sessionsInput.toIntOrNull() ?: -1) > 0) {
                _uiState.update { uiState ->
                    uiState.copy(
                        isSessionError = false
                    )
                }
                if (name != "") {
                    _uiState.update { uiState ->
                        uiState.copy(
                            enableSave = true
                        )
                    }
                }
            } else {
                _uiState.update { uiState ->
                    uiState.copy(
                        isSessionError = true,
                        enableSave = false,

                    )
                }
            }
        }

        fun saveMember(onSaved: () -> Unit) {
            viewModelScope.launch {
                val notesText = buildString {
                    if (medicalConcern.isNotBlank()) {
                        append("Medical concern: ")
                        append(medicalConcern)
                    }
                    if (medicalNote.isNotBlank()) {
                        if (isNotBlank()) append(", ")
                        append("Note: ")
                        append(medicalNote)
                    }
                    if (birthday.isNotBlank()) {
                        if (isNotBlank()) append(", ")
                        append("Birthday: ")
                        append(birthday)
                    }
                    if (sex.isNotBlank()) {
                        if (isNotBlank()) append(", ")
                        append("Sex: ")
                        append(sex)
                    }
                    if (weight.isNotBlank()) {
                        if (isNotBlank()) append(", ")
                        append("Weight: ")
                        append(weight)
                        append(" ")
                        append(weightUnit)
                    }
                    if (height.isNotBlank()) {
                        if (isNotBlank()) append(", ")
                        append("Height: ")
                        append(height)
                        append(" ")
                        append(heightUnit)
                    }
                }

                val member = MemberEntity(
                    name = name,
                    joinDate = System.currentTimeMillis(),
                    endDate = null,
                    fitnessLevel = "Beginner",
                    goal = goal.ifBlank { "General fitness" },
                    notes = notesText,
                    imageUri = null,
                    status = "Active"
                )

                repository.addMember(member)
                onSaved()
            }
        }
    }