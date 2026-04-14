package com.example.cs551fitnessapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserAppointmentDao { //This is SessionDao

    @Insert
    suspend fun insert(sessions: SessionEntity): Long

    @Query("SELECT * FROM sessions WHERE sessionId = :id")
    suspend fun getEventById(id: Int): SessionEntity?

    @Query("SELECT * FROM sessions")
    fun getAllEvents(): kotlinx.coroutines.flow.Flow<List<SessionEntity>>

    @Query("SELECT sum(duration) FROM sessions") //should be last 7 days
    suspend fun getTotalHourTraining(): Double?

    @Query("SELECT sum(duration) FROM sessions where dtStartSession >= :startTime and dtStartSession <= :currentTime") //should be last 7 days
    suspend fun getTotalHourTrainingWeekly(startTime: Long, currentTime: Long): Double?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(events: List<SessionEntity>): List<Long>

    @Query("UPDATE sessions SET dtStartSession = :dtStartSession WHERE ownerUserId = :id")
    suspend fun updateDtStartOnly(id: Int, dtStartSession: Long): Int
}