package com.example.cs551fitnessapp.ui.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.cs551fitnessapp.database.DatabaseModule
import com.example.cs551fitnessapp.database.member.MemberEntity
import com.example.cs551fitnessapp.ui.viewmodels.states.MembersUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MembersViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DatabaseModule.provideMemberRepository(application)

    private val _uiState = MutableStateFlow(MembersUiState())
    val uiState: StateFlow<MembersUiState> = _uiState.asStateFlow()

    var searchEntry by mutableStateOf("")

    init {
        observeMembers()
    }

    private fun observeMembers() {
        viewModelScope.launch {
            repository.getAllMembers().collectLatest { members ->
                _uiState.update { uiState ->
                    uiState.copy(
                        members = members,
                        sortedMembers = filterAndSearchMembers(
                            members = members,
                            query = searchEntry,
                            includeActive = uiState.includeActive,
                            includeInactive = uiState.includeInactive,
                            includeNearlyFinished = uiState.includeNearlyFinished
                        )
                    )
                }
            }
        }
    }

    fun pressActiveButton() {
        _uiState.update { uiState ->
            uiState.copy(includeActive = !uiState.includeActive)
        }
        doSearch()
    }

    fun pressNearlyFinishedButton() {
        _uiState.update { uiState ->
            uiState.copy(includeNearlyFinished = !uiState.includeNearlyFinished)
        }
        doSearch()
    }

    fun pressInactiveButton() {
        _uiState.update { uiState ->
            uiState.copy(includeInactive = !uiState.includeInactive)
        }
        doSearch()
    }

    fun doSearch() {
        val ui = _uiState.value
        val sortedMembers = filterAndSearchMembers(
            members = ui.members,
            query = searchEntry,
            includeActive = ui.includeActive,
            includeInactive = ui.includeInactive,
            includeNearlyFinished = ui.includeNearlyFinished
        )
        _uiState.update { it.copy(sortedMembers = sortedMembers) }
    }

    fun deleteMember(member: MemberEntity) {
        viewModelScope.launch {
            repository.deleteMember(member)
        }
    }

    private fun filterAndSearchMembers(
        members: List<MemberEntity>,
        query: String,
        includeActive: Boolean,
        includeInactive: Boolean,
        includeNearlyFinished: Boolean
    ): List<MemberEntity> {
        val searched = if (query.isBlank()) {
            members
        } else {
            members.filter { it.name.contains(query, ignoreCase = true) }
        }

        return searched.filter {
            (it.status == "Active" && includeActive) ||
                    (it.status == "Inactive" && includeInactive) ||
                    (it.status == "Nearly Finished" && includeNearlyFinished)
        }
    }

    // Testing the database actually works
    /**
    fun insertTestMember() {
        viewModelScope.launch {
            repository.addMember(
                MemberEntity(
                    name = "Jessica J.",
                    joinDate = System.currentTimeMillis(),
                    endDate = null,
                    fitnessLevel = "Beginner",
                    goal = "Lose weight",
                    notes = "Test member",
                    imageUri = null,
                    status = "Active"
                )
            )
        }
    }
    **/
}