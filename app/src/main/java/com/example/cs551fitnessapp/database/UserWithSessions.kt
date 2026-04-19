package com.example.cs551fitnessapp.database

import androidx.room.Embedded
import androidx.room.Relation
import com.example.cs551fitnessapp.database.SessionEntity
import com.example.cs551fitnessapp.database.UserEntity

// One user with all their sessions
data class UserWithSessions(
    @Embedded
    val user     : UserEntity,

    @Relation(
        parentColumn = "userId",
        entityColumn = "ownerUserId"
    )
    val sessions : List<SessionEntity>
)
