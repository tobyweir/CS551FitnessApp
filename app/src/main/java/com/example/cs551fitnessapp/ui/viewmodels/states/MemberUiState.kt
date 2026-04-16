package com.example.cs551fitnessapp.ui.viewmodels.states

data class MemberUiState(
    val memberId: Long = 0,
    val name: String = "",
    val joinDate: Long? = null,
    val endDate: Long? = null,
    val fitnessLevel: String = "",
    val goal: String = "",
    val notes: String = "",
    val imageUri: String? = null,
    val status: String = ""
)