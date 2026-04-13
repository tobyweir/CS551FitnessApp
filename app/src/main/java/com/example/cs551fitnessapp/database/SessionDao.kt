package com.example.cs551fitnessapp.database

import androidx.room.*
import com.example.cs551fitnessapp.database.SessionEntity
import com.example.cs551fitnessapp.database.SessionWithExercises
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: SessionEntity): Long

//    @Update
//    suspend fun updateSession(session: SessionEntity)
//
    @Delete
    suspend fun deleteSession(session: SessionEntity)

    @Query("SELECT * FROM sessions WHERE sessionId = :id")
    suspend fun getSessionById(id: Long): SessionEntity?

    // All sessions for a specific user, newest first
    @Query("""
        SELECT * FROM sessions 
        WHERE ownerUserId = :userId 
    """)
    fun getSessionsByUser(userId: Long): Flow<List<SessionEntity>>

    // Sessions for a user on a specific date e.g. "12 Apr 2026"
    @Query("""
        SELECT * FROM sessions
        WHERE ownerUserId = :userId AND dtStartSession = :date
        ORDER BY dtStartSession DESC
    """)
    fun getSessionsByDate(userId: Long, date: String): Flow<List<SessionEntity>>

    // Full session with exercise list
    @Transaction
    @Query("SELECT * FROM sessions WHERE sessionId = :sessionId")
    fun getSessionWithExercises(sessionId: Long): Flow<SessionWithExercises>

    // All sessions with exercises for a user
    @Transaction
    @Query("""
        SELECT * FROM sessions 
        WHERE ownerUserId = :userId
    """)
    fun getAllSessionsWithExercises(userId: Long): Flow<List<SessionWithExercises>>
}