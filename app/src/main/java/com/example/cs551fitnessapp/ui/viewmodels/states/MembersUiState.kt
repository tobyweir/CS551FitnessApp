package com.example.cs551fitnessapp.ui.viewmodels.states

import com.example.cs551fitnessapp.ui.screens.Member


data class MembersUiState(
    val members: List<Member>,
    val sortedMembers : List<Member>,
    val includeActive : Boolean,
    val includeInactive : Boolean,
    val includeNearlyFinished : Boolean,
    val isError : Boolean,
)
