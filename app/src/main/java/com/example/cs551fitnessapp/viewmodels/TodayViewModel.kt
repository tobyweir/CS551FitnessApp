package com.example.cs551fitnessapp.viewmodels

import androidx.lifecycle.ViewModel
import com.example.cs551fitnessapp.viewmodels.states.MembersUiState
import com.example.cs551fitnessapp.viewmodels.states.TodayUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow



class TodayViewModel : ViewModel() {
    private val _uiState  = MutableStateFlow(TodayUiState(0))
    val uiState : StateFlow<TodayUiState> = _uiState.asStateFlow()
}