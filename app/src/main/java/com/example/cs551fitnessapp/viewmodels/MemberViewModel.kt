package com.example.cs551fitnessapp.viewmodels

import androidx.lifecycle.ViewModel
import com.example.cs551fitnessapp.viewmodels.states.MemberUiState
import com.example.cs551fitnessapp.viewmodels.states.MembersUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MemberViewModel : ViewModel() {
    private val _uiState  = MutableStateFlow(MemberUiState(""))
    val uiState : StateFlow<MemberUiState> = _uiState.asStateFlow()
}