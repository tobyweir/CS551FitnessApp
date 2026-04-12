package com.example.cs551fitnessapp.database


data class WorkoutEntry(
    val exercise : Exercise,
    val sets     : Int    = 3,
    val reps     : Int    = 12,
    val timeHr  : Int    = 0,
    val timeMin  : Int    = 0,
    val note     : String = ""   // e.g. "40 lbs"
)