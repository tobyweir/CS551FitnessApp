package com.example.cs551fitnessapp.database.member
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "members")
data class MemberEntity(
    @PrimaryKey(autoGenerate = true)
    val memberId     : Long    = 0,
    val name         : String,
    val joinDate     : Long,
    val endDate      : Long?   = null,
    val fitnessLevel : String  = "",
    val goal         : String  = "",
    val notes        : String  = "",
    val imageUri     : String? = null,
    val status       : String  = "Active" // I made the default active since I'm assuming when a member first joins they are going to be active. Could change this later though.
)