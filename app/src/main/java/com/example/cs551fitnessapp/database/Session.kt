package com.example.cs551fitnessapp.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.cs551fitnessapp.database.member.MemberEntity

@Entity(
    tableName = "sessions",
    foreignKeys = [
        ForeignKey(
            entity = MemberEntity::class,
            parentColumns = ["memberId"],
            childColumns = ["ownerMemberId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["ownerMemberId"])]
)
data class SessionEntity(
    @PrimaryKey(autoGenerate = true)
    val sessionId: Long = 0,
    val ownerMemberId: Long,
    val sessionName: String,
    val dtStartSession: Long,
    val dtEndSession: Long,
    val duration: Long
)
