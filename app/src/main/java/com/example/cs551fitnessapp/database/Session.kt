package com.example.cs551fitnessapp.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "sessions",
    foreignKeys = [
        ForeignKey(
            entity        = UserEntity::class,
            parentColumns = ["userId"],
            childColumns  = ["ownerUserId"],
            onDelete      = ForeignKey.CASCADE
        )
    ],
    indices = [Index("ownerUserId")]
)
data class SessionEntity(
    @PrimaryKey(autoGenerate = true)
    val sessionId   : Int   = 0,
    val ownerUserId     : Int,              // FK  users.userId
    val sessionName : String,
    val dtStartSession : Long,
    val dtEndSession   : Long,
    val duration    : Double
)
