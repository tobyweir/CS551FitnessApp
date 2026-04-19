package com.example.cs551fitnessapp.ui.viewmodels.states

data class SessionExerciseUi(
    val entryId: Long,
    val exerciseName: String,
    val bodyParts: String,
    val gifUrl: String?,
    val sets: Int,
    val reps: Int,
    val timeHr: Int,
    val timeMin: Int,
    val note: String
)

data class SessionDetailUiState(
    val sessionId: Long = 0,
    val sessionName: String = "",
    val memberId: Long = 0,
    val memberName: String = "",
    val startText: String = "",
    val endText: String = "",
    val durationText: String = "",
    val dateText: String = "",
    val exercises: List<SessionExerciseUi> = emptyList(),
    val isLoading: Boolean = true,
    val notFound: Boolean = false
)