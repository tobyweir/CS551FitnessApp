package com.example.cs551fitnessapp.database


import androidx.room.*
import com.example.cs551fitnessapp.database.SessionExerciseEntity
import com.example.cs551fitnessapp.database.SessionExerciseWithDetail
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionExerciseDao {

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertSessionExercise(entry: SessionExerciseEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entries: List<SessionExerciseEntity>)

    @Update
    suspend fun updateSessionExercise(entry: SessionExerciseEntity)

    @Delete
    suspend fun deleteSessionExercise(entry: SessionExerciseEntity)

    @Query("DELETE FROM session_exercises WHERE ownerSessionId = :sessionId")
    suspend fun deleteAllForSession(sessionId: Long)

    // All exercise entries for a session, joined with exercise detail
    @Query("""
        SELECT 
            se.*,
            e.exerciseId  AS ex_exerciseId,
            e.name        AS ex_name,
            e.gifUrl      AS ex_gifUrl,
            e.bodyParts AS ex_bodyParts,
            e.cachedAt    AS ex_cachedAt
        FROM session_exercises se
        INNER JOIN exercises e ON se.exerciseRefId = e.exerciseId
        WHERE se.ownerSessionId = :sessionId
    """)
    fun getExercisesForSession(sessionId: Long): Flow<List<SessionExerciseWithDetail>>
}