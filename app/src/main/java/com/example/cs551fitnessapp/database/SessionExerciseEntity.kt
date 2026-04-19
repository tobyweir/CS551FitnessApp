package com.example.cs551fitnessapp.database

// Table for store workout info in the session

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "session_exercises",
    foreignKeys = [
        ForeignKey(
            entity        = SessionEntity::class,
            parentColumns = ["sessionId"],
            childColumns  = ["ownerSessionId"],
            onDelete      = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity        = ExerciseEntity::class,
            parentColumns = ["exerciseId"],
            childColumns  = ["exerciseRefId"],
            onDelete      = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("ownerSessionId"),
        Index("exerciseRefId")
    ]
)
data class SessionExerciseEntity(
    @PrimaryKey(autoGenerate = true)
    val id             : Long   = 0,
    val ownerSessionId : Long,           // FK → sessions.sessionId
    val exerciseRefId    : String,       // FK → exercises.exerciseId
    val sets           : Int,
    val reps           : Int,
    val timeHr         : Int,
    val timeMin        : Int,
    val note           : String = "",    // e.g. "40 lbs"
)
