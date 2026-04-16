package com.example.cs551fitnessapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// Needs to be refactored, it uses userFirstname
@Entity(tableName = "userAppointment") //unused for now
data class UserAppointmentEntity(
    @PrimaryKey val id: Int,
    val userFirstname: String,
    val dtStartSession: Long,
    val dtEndSession: Long,
    val duration: Double
)

