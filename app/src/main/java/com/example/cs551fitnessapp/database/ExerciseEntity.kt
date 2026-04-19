package com.example.cs551fitnessapp.database

// Table for store master data of exercises from API

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class ExerciseEntity(
    @PrimaryKey
    val exerciseId      : String,        // exerciseId from API
    val name            : String,
    val gifUrl          : String?,
    val bodyParts       : String,        // stored as comma-separated string
    val cachedAt        : Long = System.currentTimeMillis()
)
