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

    private var nextId = 100



    private val initialMembers = listOf(

        Member(0,"John Smith", Date(2026 ,3 ,17), null ,"Active"),

        Member(1,"Mike Smith", Date(2026 ,3 ,17), null ,"Inactive"),

        Member(2,"Major Smith", Date(2026 ,3 ,17), null ,"Inactive"),

        Member(3,"Anna Lee", Date(2026 ,3 ,17), null ,"Nearly Finished")

    )



    private val _uiState = MutableStateFlow(

        MembersUiState(

            members = initialMembers,

            sortedMembers = initialMembers,

            includeActive = true,

            includeInactive = true,

            includeNearlyFinished = true,

            isError = false

        )

    )



    val uiState: StateFlow<MembersUiState> = _uiState.asStateFlow()



    var searchEntry by mutableStateOf("")



    fun addMember(

        name: String,

        sessions: Int

    ) {

        val newMember = Member(

            id = nextId++,

            name = name,

            joinDate = Date(),

            endDate = null,

            status = "Active"

        )



        val updatedList =

            listOf(newMember) +

                    _uiState.value.members



        _uiState.update {

            it.copy(

                members = updatedList,

                sortedMembers = updatedList

            )

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



    fun pressInactiveButton() {

        _uiState.update {

            it.copy(

                includeInactive = !it.includeInactive

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



    fun doSearch() {

        var filtered = _uiState.value.members



        if (searchEntry.isNotEmpty()) {

            filtered = filtered.filter {

                it.name.contains(searchEntry,true)

            }

        }



        filtered = filterByButtons(filtered)



        _uiState.update {

            it.copy(

                sortedMembers = filtered

            )

        }

    }



    private fun filterByButtons(

        members: List<Member>

    ): List<Member> {



        val active = _uiState.value.includeActive

        val inactive = _uiState.value.includeInactive

        val nearly = _uiState.value.includeNearlyFinished



        return members.filter {

            (it.status == "Active" && active) ||

                    (it.status == "Inactive" && inactive) ||

                    (it.status == "Nearly Finished" && nearly)

        }

    }

}