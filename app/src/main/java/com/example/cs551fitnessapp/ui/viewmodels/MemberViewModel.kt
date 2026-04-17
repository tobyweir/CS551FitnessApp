package com.example.cs551fitnessapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.cs551fitnessapp.database.DatabaseModule
import com.example.cs551fitnessapp.ui.viewmodels.states.MemberSessionItem
import com.example.cs551fitnessapp.ui.viewmodels.states.MemberUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MemberViewModel(application: Application) : AndroidViewModel(application) {

    private val memberRepository = DatabaseModule.provideMemberRepository(application)
    private val workoutRepository = DatabaseModule.provideRepository(application)

    private val _uiState = kotlinx.coroutines.flow.MutableStateFlow(MemberUiState())
    val uiState: kotlinx.coroutines.flow.StateFlow<MemberUiState> = _uiState

    private var loadJob: Job? = null

    fun loadMember(memberId: Long) {
        loadJob?.cancel()

        _uiState.value = MemberUiState(isLoading = true)

        loadJob = viewModelScope.launch {
            combine(
                memberRepository.getMemberById(memberId),
                workoutRepository.getSessionsByMember(memberId)
            ) { member, sessions ->
                if (member == null) {
                    MemberUiState(isLoading = false)
                } else {
                    val now = System.currentTimeMillis()

                    val mappedSessions = sessions
                        .sortedByDescending { it.dtStartSession }
                        .map { session ->
                            MemberSessionItem(
                                sessionId = session.sessionId,
                                sessionName = session.sessionName,
                                startText = formatTime(session.dtStartSession),
                                durationText = formatDuration(session.duration),
                                endText = formatTime(session.dtEndSession),
                                isUpcoming = session.dtStartSession >= now
                            )
                        }

                    MemberUiState(
                        memberId = member.memberId,
                        name = member.name,
                        joinDate = member.joinDate,
                        endDate = member.endDate,
                        fitnessLevel = member.fitnessLevel,
                        goal = member.goal,
                        notes = member.notes,
                        imageUri = member.imageUri,
                        status = member.status,
                        upcomingSessions = mappedSessions.filter { it.isUpcoming }
                            .sortedBy { it.startText },
                        previousSessions = mappedSessions.filter { !it.isUpcoming },
                        isLoading = false
                    )
                }
            }.collectLatest { newState ->
                _uiState.value = newState
            }
        }
    }

    private fun formatTime(timestamp: Long): String {
        return SimpleDateFormat("d MMM yyyy  h:mm a", Locale.getDefault())
            .format(Date(timestamp))
    }

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