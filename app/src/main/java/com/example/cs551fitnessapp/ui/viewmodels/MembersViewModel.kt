package com.example.cs551fitnessapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.cs551fitnessapp.ui.viewmodels.states.MembersUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MembersViewModel : ViewModel() {
    private val _uiState  = MutableStateFlow(MembersUiState(listOf()))
    val uiState : StateFlow<MembersUiState> = _uiState.asStateFlow()
}