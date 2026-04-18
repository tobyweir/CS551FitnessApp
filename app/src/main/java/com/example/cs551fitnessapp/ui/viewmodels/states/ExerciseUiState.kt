package com.example.cs551fitnessapp.ui.viewmodels.states

import com.example.cs551fitnessapp.database.Exercise

sealed class ExerciseUiState {
    object Idle    : ExerciseUiState()
    object Loading : ExerciseUiState()
    data class Success(val exercises: List<Exercise>) : ExerciseUiState()
    data class Error(val message: String)             : ExerciseUiState()
    object NoInternet : ExerciseUiState()
}