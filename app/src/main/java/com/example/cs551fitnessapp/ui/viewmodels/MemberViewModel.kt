package com.example.cs551fitnessapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.cs551fitnessapp.ui.viewmodels.states.MemberUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MemberViewModel : ViewModel() {
    private val _uiState  = MutableStateFlow(MemberUiState(""))
    val uiState : StateFlow<MemberUiState> = _uiState.asStateFlow()
}