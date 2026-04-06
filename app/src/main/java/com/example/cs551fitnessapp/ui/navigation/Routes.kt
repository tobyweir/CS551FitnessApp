package com.example.cs551fitnessapp.ui.navigation

import kotlinx.serialization.Serializable


@Serializable
object Today

@Serializable
object Members

@Serializable
data class EditMember(val id: Int)

@Serializable
object PreferencesPage;