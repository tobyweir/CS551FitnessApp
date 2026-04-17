package com.example.cs551fitnessapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.cs551fitnessapp.database.DatabaseModule
import com.example.cs551fitnessapp.ui.viewmodels.states.MemberUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MemberViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DatabaseModule.provideMemberRepository(application)

    private val _uiState = MutableStateFlow(MemberUiState())
    val uiState: StateFlow<MemberUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    fun loadMember(memberId: Long) {
        loadJob?.cancel()

        _uiState.value = MemberUiState(isLoading = true)

        loadJob = viewModelScope.launch {
            repository.getMemberById(memberId).collectLatest { member ->
                _uiState.value =
                    if (member != null) {
                        MemberUiState(
                            memberId = member.memberId,
                            name = member.name,
                            joinDate = member.joinDate,
                            goal = member.goal,
                            notes = member.notes,
                            status = member.status,
                            imageUri = member.imageUri,
                            isLoading = false
                        )
                    } else {
                        MemberUiState(isLoading = false)
                    }
            }
        }
    }
}