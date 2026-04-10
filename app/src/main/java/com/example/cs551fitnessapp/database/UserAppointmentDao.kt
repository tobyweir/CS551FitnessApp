package com.example.cs551fitnessapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserAppointmentDao {

    @Insert
    suspend fun insert(userAppointment: UserAppointmentEntity): Long

    @Query("SELECT * FROM userAppointment WHERE id = :id")
    suspend fun getEventById(id: Int): UserAppointmentEntity?

    @Query("SELECT * FROM userAppointment")
    fun getAllEvents(): kotlinx.coroutines.flow.Flow<List<UserAppointmentEntity>>

    @Query("SELECT sum(duration) FROM userAppointment") //should be last 7 days
    suspend fun getTotalHourTraining(): Double?

    @Query("SELECT sum(duration) FROM userAppointment where dtStartSession >= :startTime and dtStartSession <= :currentTime") //should be last 7 days
    suspend fun getTotalHourTrainingWeekly(startTime: Long, currentTime: Long): Double?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(events: List<UserAppointmentEntity>): List<Long>

    @Query("UPDATE userAppointment SET dtStartSession = :dtStartSession WHERE id = :id")
    suspend fun updateDtStartOnly(id: Int, dtStartSession: Long): Int
}