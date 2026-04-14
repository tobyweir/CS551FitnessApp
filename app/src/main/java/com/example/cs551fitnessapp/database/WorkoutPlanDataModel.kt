package com.example.cs551fitnessapp.database

data class WorkoutPlanData(
    val sessionName : String,
    val date        : String,
    val startHour   : Int,
    val startMin    : Int,
    val endHour     : Int,
    val endMin      : Int,
    val entries     : List<WorkoutEntry>
)