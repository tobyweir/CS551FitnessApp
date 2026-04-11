package com.example.cs551fitnessapp.ui.viewmodels.states

import com.example.cs551fitnessapp.ui.screens.Member
import com.example.cs551fitnessapp.ui.screens.Workout
import java.time.LocalDate
import java.util.Date

data class TodayUiState(
    val day1 : LocalDate,
    val day2 : LocalDate,
    val day3 : LocalDate,
    val day4 : LocalDate,
    val day5 : LocalDate,
    val day6 : LocalDate,
    val day7 : LocalDate,
    val selectedDay : LocalDate,
    val workouts : List<Workout>,
    val filteredWorkouts : List<Workout>,
    val members : List<Member>
)
