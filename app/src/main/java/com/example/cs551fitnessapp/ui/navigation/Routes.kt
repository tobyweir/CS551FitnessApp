package com.example.cs551fitnessapp.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object Today

@Serializable
object Members

@Serializable
object PreferencesPage

@Serializable
data class MemberPage(val id: Int)

@Serializable
data class AddWorkoutFlow(val id: Int)

@Serializable
object AddMemberFlow