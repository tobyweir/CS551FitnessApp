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
        Member(
            id = 0,
            name ="John Smith" ,
            joinDate = Date(2026 , 3 , 17) ,
            endDate = null ,
            status = "Active",),
        Member( id = 1,
            name ="Mike Smith" ,
            joinDate = Date(2026 , 3 , 17) ,
            endDate = null ,
            status = "Inactive",),
        Member(
            id = 2,
            name ="Major Smith" ,
            joinDate = Date(2026 , 3 , 17) ,
            endDate = null ,
            status = "Inactive",),
        Member(
            id = 3,
            name ="John Smith" ,
            joinDate = Date(2026 , 3 , 17) ,
            endDate = null ,
            status = "Nearly Finished",),
        Member( id = 4,
            name ="Mike Smith" ,
            joinDate = Date(2026 , 3 , 17) ,
            endDate = null ,
            status = "Active",),
        Member(
            id = 5,
            name ="Major Smith" ,
            joinDate = Date(2026 , 3 , 17) ,
            endDate = null ,
            status = "Nearly Finished",),
        Member(
            id = 6,
            name ="John Smith" ,
            joinDate = Date(2026 , 3 , 17) ,
            endDate = null ,
            status = "Active",),
        Member( id = 7,
            name ="Mike Smith" ,
            joinDate = Date(2026 , 3 , 17) ,
            endDate = null ,
            status = "Inactive",),
        Member(
            id = 8,
            name ="Major Smith" ,
            joinDate = Date(2026 , 3 , 17) ,
            endDate = null ,
            status = "Nearly Finished",)
    )
    private val _uiState  = MutableStateFlow(MembersUiState(
        members = members ,
        sortedMembers = members ,
        includeActive = true ,
        includeInactive = true,
        includeNearlyFinished = true,
        isError = false,
    ))

    val uiState : StateFlow<MembersUiState> = _uiState.asStateFlow()

    fun pressActiveButton () {
        _uiState.update { uiState ->
            uiState.copy(
                includeActive = !_uiState.value.includeActive
            )
        }
        doSearch()
    }

    fun pressNearlyFinishedButton () {
        _uiState.update { uiState ->
            uiState.copy(
                includeNearlyFinished = !_uiState.value.includeNearlyFinished
            )
        }
        doSearch()
    }

    fun pressInactiveButton () {
        _uiState.update { uiState ->
            uiState.copy(
                includeInactive = !_uiState.value.includeInactive
            )
        }
        doSearch()
    }
    fun doSearch () {
        val members = _uiState.value.members
        var sortedMembers : List<Member>
        if (searchEntry == "") {
            sortedMembers = members
        } else {
            sortedMembers = members.filter { it.name.lowercase().contains(searchEntry.lowercase()) }
        }
        sortedMembers = filterByButtons(sortedMembers)
        updateState(sortedMembers)
    }

     fun filterByButtons (members : List<Member>) : List<Member> {
        val isActive =_uiState.value.includeActive
        val isInactive = _uiState.value.includeInactive
        val isNearlyFinished = _uiState.value.includeNearlyFinished
         var sortedMembers = members
         sortedMembers = sortedMembers.filter { (it.status == "Active" && isActive) ||
                 (it.status == "Inactive" && isInactive) ||
                 (it.status == "Nearly Finished" && isNearlyFinished)}
         return sortedMembers
    }
    var searchEntry by mutableStateOf("")

    private fun updateState () {
        _uiState.update { uiState ->
            uiState.copy(
                includeActive = !_uiState.value.includeActive
            )
        }
    }

    private fun updateState (sortedMembers : List<Member>) {
        _uiState.update { uiState ->
            uiState.copy(
                sortedMembers = sortedMembers
            )
        }
    }


}