package com.example.cs551fitnessapp.ui.viewmodels.states

import com.example.cs551fitnessapp.database.member.MemberEntity

data class MembersUiState(
    val members: List<MemberEntity> = emptyList(),
    val sortedMembers: List<MemberEntity> = emptyList(),
    val includeActive: Boolean = true,
    val includeInactive: Boolean = true,
    val includeNearlyFinished: Boolean = true,
    val isError: Boolean = false,
)
