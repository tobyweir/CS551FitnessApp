package com.example.cs551fitnessapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.cs551fitnessapp.ui.viewmodels.states.EditMemberUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class EditMemberViewModel : ViewModel() {
    private val _uiState  = MutableStateFlow(EditMemberUiState(0))
    val uiState : StateFlow<EditMemberUiState> = _uiState.asStateFlow()
}