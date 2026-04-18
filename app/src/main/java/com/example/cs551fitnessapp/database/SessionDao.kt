package com.example.cs551fitnessapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {

    @Insert
    suspend fun insertSession(session: SessionEntity): Long

    @Delete
    suspend fun deleteSession(session: SessionEntity)

    @Query("SELECT * FROM sessions WHERE sessionId = :sessionId")
    suspend fun getSessionById(sessionId: Long): SessionEntity?

    @Query("SELECT * FROM sessions WHERE ownerMemberId = :memberId ORDER BY dtStartSession ASC")
    fun getSessionsByMember(memberId: Long): Flow<List<SessionEntity>>

    @Query("""
        SELECT * FROM sessions
        WHERE ownerMemberId = :memberId
        AND dtStartSession >= :startOfDay
        AND dtStartSession <= :endOfDay
        ORDER BY dtStartSession ASC
    """)
    fun getSessionsForMemberInRange(
        memberId: Long,
        startOfDay: Long,
        endOfDay: Long
    ): Flow<List<SessionEntity>>

    @Transaction
    @Query("SELECT * FROM sessions WHERE sessionId = :sessionId")
    fun getSessionWithExercises(sessionId: Long): Flow<SessionWithExercises?>

    @Transaction
    @Query("SELECT * FROM sessions WHERE ownerMemberId = :memberId ORDER BY dtStartSession DESC")
    fun getAllSessionsWithExercises(memberId: Long): Flow<List<SessionWithExercises>>

    @Query("""
        SELECT COUNT(*) FROM sessions
        WHERE :startAt < dtEndSession
        AND :endAt > dtStartSession
    """)
    suspend fun countOverlappingSessions(
        startAt: Long,
        endAt: Long
    ): Int

    @Query("""
        SELECT * FROM sessions
        WHERE dtStartSession >= :startAt
        AND dtStartSession <= :endAt
        ORDER BY dtStartSession ASC
    """)
    fun getAllSessionsInRange(startAt: Long, endAt: Long): Flow<List<SessionEntity>>
}