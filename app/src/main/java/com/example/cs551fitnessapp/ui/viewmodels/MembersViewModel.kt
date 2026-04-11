package com.example.cs551fitnessapp.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.cs551fitnessapp.ui.screens.Member
import com.example.cs551fitnessapp.ui.viewmodels.states.MembersUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Date

class MembersViewModel : ViewModel() {
    val members = listOf<Member>(
        Member(id = 0,
            name ="John Smith" ,
            joinDate = Date(2026 , 3 , 17) ,
            endDate = null ,
            status = "Active",),
        Member( id = 1,
            name ="Mike Smith" ,
            joinDate = Date(2026 , 3 , 17) ,
            endDate = null ,
            status = "Active",),
        Member(id = 2,
            name ="Major Smith" ,
            joinDate = Date(2026 , 3 , 17) ,
            endDate = null ,
            status = "Active",),
        Member(id = 3,
            name ="John Smith" ,
            joinDate = Date(2026 , 3 , 17) ,
            endDate = null ,
            status = "Active",),
        Member( id = 4,
            name ="Mike Smith" ,
            joinDate = Date(2026 , 3 , 17) ,
            endDate = null ,
            status = "Active",),
        Member(id = 5,
            name ="Major Smith" ,
            joinDate = Date(2026 , 3 , 17) ,
            endDate = null ,
            status = "Active",),
        Member(id = 6,
            name ="John Smith" ,
            joinDate = Date(2026 , 3 , 17) ,
            endDate = null ,
            status = "Active",),
        Member( id = 7,
            name ="Mike Smith" ,
            joinDate = Date(2026 , 3 , 17) ,
            endDate = null ,
            status = "Active",),
        Member(id = 8,
            name ="Major Smith" ,
            joinDate = Date(2026 , 3 , 17) ,
            endDate = null ,
            status = "Active",)
    )
    private val _uiState  = MutableStateFlow(MembersUiState(
        listOf() ,
        listOf() ,
        includeActive = true ,
        includeInactive = true,
        includeNearlyFinished = true,
        isError = false,
    ))

    val uiState : StateFlow<MembersUiState> = _uiState.asStateFlow()

    fun doSearch () {

    }
    var searchEntry by mutableStateOf("")

    private fun updateState () {
        _uiState.update { uiState ->
            uiState.copy(

            )
        }
    }
}