package com.example.cs551fitnessapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.cs551fitnessapp.database.DatabaseModule
import com.example.cs551fitnessapp.ui.viewmodels.states.SessionDetailUiState
import com.example.cs551fitnessapp.ui.viewmodels.states.SessionExerciseUi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class SessionDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val workoutRepository = DatabaseModule.provideRepository(application)
    private val memberRepository = DatabaseModule.provideMemberRepository(application)

    private val _uiState = MutableStateFlow(SessionDetailUiState())
    val uiState: StateFlow<SessionDetailUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    fun loadSession(sessionId: Long) {
        loadJob?.cancel()
        _uiState.value = SessionDetailUiState(isLoading = true)

        loadJob = viewModelScope.launch {
            workoutRepository.getSessionWithExercises(sessionId)
                .flatMapLatest { sessionWithExercises ->
                    if (sessionWithExercises == null) {
                        flowOf(Triple(null, emptyList(), null))
                    } else {
                        val session = sessionWithExercises.session
                        combine(
                            workoutRepository.getExercisesForSession(session.sessionId),
                            memberRepository.getMemberById(session.ownerMemberId)
                        ) { exerciseDetails, member ->
                            Triple(session, exerciseDetails, member)
                        }
                    }
                }
                .collectLatest { (session, exerciseDetails, member) ->
                    if (session == null) {
                        _uiState.value = SessionDetailUiState(
                            isLoading = false,
                            notFound = true
                        )
                        return@collectLatest
                    }

                    _uiState.value = SessionDetailUiState(
                        sessionId = session.sessionId,
                        sessionName = session.sessionName,
                        memberId = session.ownerMemberId,
                        memberName = member?.name ?: "Unknown Member",
                        startText = formatTime(session.dtStartSession),
                        endText = formatTime(session.dtEndSession),
                        durationText = formatDuration(session.duration),
                        dateText = formatDate(session.dtStartSession),
                        exercises = exerciseDetails.map { detail ->
                            SessionExerciseUi(
                                entryId = detail.entry.id,
                                exerciseName = detail.exercise.name,
                                bodyParts = detail.exercise.bodyParts,
                                gifUrl = detail.exercise.gifUrl,
                                sets = detail.entry.sets,
                                reps = detail.entry.reps,
                                timeHr = detail.entry.timeHr,
                                timeMin = detail.entry.timeMin,
                                note = detail.entry.note
                            )
                        },
                        isLoading = false,
                        notFound = false
                    )
                }
        }
    }

    private fun formatTime(timestamp: Long): String =
        SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(timestamp))

    private fun formatDate(timestamp: Long): String =
        SimpleDateFormat("EEEE, d MMM yyyy", Locale.getDefault()).format(Date(timestamp))

    private fun formatDuration(duration: Long): String {
        val totalMinutes = duration.toInt()
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        return when {
            hours > 0 && minutes > 0 -> "${hours} hr ${minutes} min"
            hours > 0 -> "${hours} hr"
            else -> "${minutes} min"
        }
    }
}