package com.example.cs551fitnessapp.ui.viewmodels.states

data class AddMemberUiState(
    val isSexError : Boolean,
    val enableSexNext : Boolean,
    val isBirthdayError : Boolean,
    val enableBirthdayNext : Boolean,
    val isWeightError : Boolean,
    val enableWeightNext : Boolean,
    val isHeightError : Boolean,
    val enableHeightNext : Boolean,
    val isGoalError : Boolean,
    val enableGoalNext : Boolean,
    val isMedicalConcernError : Boolean,
    val enableMedicalNext : Boolean,
    val isNameError : Boolean,
    val isSessionError : Boolean,
    val enableSave : Boolean,
)
