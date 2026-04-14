package com.example.cs551fitnessapp.database

// -- relation with session and exercise ----------------------------------------------

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.cs551fitnessapp.database.ExerciseEntity
import com.example.cs551fitnessapp.database.SessionEntity
import com.example.cs551fitnessapp.database.SessionExerciseEntity

/** session entry joined with its exercise detail */
data class SessionExerciseWithDetail(
    @Embedded val entry    : SessionExerciseEntity,
    @Embedded(prefix = "ex_") val exercise : ExerciseEntity
)

/** One session with all its exercises  */
data class SessionWithExercises(
    @Embedded
    val session   : SessionEntity,

    @Relation(
        parentColumn     = "sessionId",
        entityColumn     = "exerciseId",
        associateBy      = Junction(
            value             = SessionExerciseEntity::class,
            parentColumn      = "ownerSessionId",
            entityColumn      = "exerciseRefId"
        )
    )
    val exercises : List<ExerciseEntity>
)
