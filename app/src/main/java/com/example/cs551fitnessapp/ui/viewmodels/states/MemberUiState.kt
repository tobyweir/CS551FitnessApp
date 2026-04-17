package com.example.cs551fitnessapp.ui.viewmodels.states
data class MemberSessionItem(
    val sessionId: Long,
    val sessionName: String,
    val startText: String,
    val durationText: String,
    val endText: String,
    val isUpcoming: Boolean
)

data class MemberUiState(
    val memberId: Long = 0,
    val name: String = "",
    val joinDate: Long? = null,
    val endDate: Long? = null,
    val fitnessLevel: String = "",
    val goal: String = "",
    val notes: String = "",
    val imageUri: String? = null,
    val status: String = "",
    val upcomingSessions: List<MemberSessionItem> = emptyList(),
    val previousSessions: List<MemberSessionItem> = emptyList(),
    val isLoading: Boolean = true
)