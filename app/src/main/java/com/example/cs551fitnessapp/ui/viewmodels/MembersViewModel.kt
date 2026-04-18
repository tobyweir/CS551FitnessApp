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
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MembersViewModel(application: Application) : AndroidViewModel(application) {

    private val repository =
        DatabaseModule.provideMemberRepository(application)

    private val _uiState =
        MutableStateFlow(MembersUiState())

    val uiState: StateFlow<MembersUiState> =
        _uiState.asStateFlow()

    var searchEntry by mutableStateOf("")

    init {
        observeMembers()
    }

    private fun observeMembers() {

        viewModelScope.launch {

            repository.getAllMembers()

                .collectLatest { members ->

                    _uiState.update {

                        it.copy(

                            members = members,

                            sortedMembers = filterAndSearchMembers(

                                members,

                                searchEntry,

                                it.includeActive,

                                it.includeInactive,

                                it.includeNearlyFinished
                            )
                        )
                    }
                }
        }
    }

    // SAVE MEMBER WITH IMAGE
    fun addMember(

        name: String,

        sessions: Int,

        imageUri: String?

    ) {

        val endDate =

            System.currentTimeMillis()

        + sessions * 86400000L

        val member = MemberEntity(

            name = name,

            joinDate = System.currentTimeMillis(),

            endDate = endDate,

            imageUri = imageUri,

            status = "Active"
        )

        viewModelScope.launch {

            repository.addMember(member)
        }
    }

    fun deleteMember(member: MemberEntity) {

        viewModelScope.launch {

            repository.deleteMember(member)
        }
    }

    fun pressActiveButton() {

        _uiState.update {

            it.copy(

                includeActive = !it.includeActive
            )
        }

        doSearch()
    }

    fun pressNearlyFinishedButton() {

        _uiState.update {

            it.copy(

                includeNearlyFinished = !it.includeNearlyFinished
            )
        }

        doSearch()
    }

    fun pressInactiveButton() {

        _uiState.update {

            it.copy(

                includeInactive = !it.includeInactive
            )
        }

        doSearch()
    }

    fun doSearch() {

        val ui = _uiState.value

        val sortedMembers = filterAndSearchMembers(

            ui.members,

            searchEntry,

            ui.includeActive,

            ui.includeInactive,

            ui.includeNearlyFinished
        )

        _uiState.update {

            it.copy(

                sortedMembers = sortedMembers
            )
        }
    }

    private fun filterAndSearchMembers(

        members: List<MemberEntity>,

        query: String,

        includeActive: Boolean,

        includeInactive: Boolean,

        includeNearlyFinished: Boolean

    ): List<MemberEntity> {

        val searched =

            if (query.isBlank())

                members

            else

                members.filter {

                    it.name.contains(

                        query,

                        ignoreCase = true
                    )
                }

        return searched.filter {

            (it.status == "Active" && includeActive)

                    ||

                    (it.status == "Inactive" && includeInactive)

                    ||

                    (it.status == "Nearly Finished"

                            && includeNearlyFinished)
        }
    }
}