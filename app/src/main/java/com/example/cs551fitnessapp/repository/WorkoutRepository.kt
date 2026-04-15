package com.example.cs551fitnessapp.repository


import com.example.cs551fitnessapp.database.ExerciseDao
import com.example.cs551fitnessapp.database.SessionDao
import com.example.cs551fitnessapp.database.SessionExerciseDao
import com.example.cs551fitnessapp.database.UserDao
import com.example.cs551fitnessapp.database.Exercise
import com.example.cs551fitnessapp.database.ExerciseEntity
import com.example.cs551fitnessapp.database.SessionEntity
import com.example.cs551fitnessapp.database.SessionExerciseEntity
import com.example.cs551fitnessapp.database.UserEntity
import com.example.cs551fitnessapp.database.WorkoutEntry
import com.example.cs551fitnessapp.database.WorkoutPlanData
import com.example.cs551fitnessapp.ui.utils.DateTimeUtils
import com.example.cs551fitnessapp.ui.viewmodels.MemberSession
import com.example.cs551fitnessapp.ui.viewmodels.SavePlanResult

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WorkoutRepository(
    private val userDao            : UserDao,
    private val sessionDao         : SessionDao,
    private val exerciseDao        : ExerciseDao,
    private val sessionExerciseDao : SessionExerciseDao
) {

    // User

    suspend fun insertUser(name: String): Long =
        userDao.insertUser(UserEntity(name = name))


    fun getUserWithSessions(userId: Long) =
        userDao.getUserWithSessions(userId)

    // Session
    fun getalluserSessions(dtStartSession: Long, dtEndtSession: Long) : Flow<List<MemberSession>> =
        sessionDao.getSessionsbyDate(dtStartSession,dtEndtSession)     // returns Flow<List<SessionEntity>> //for debugging 1776204000000
            .map { sessions ->
                sessions.map { session ->
                    MemberSession(
                        id = session.sessionId,
                        name = session.sessionName,
                        dtSessionStart = DateTimeUtils.toDisplayString(session.dtStartSession),
                        dtSessionEnd = DateTimeUtils.toDisplayString(session.dtEndSession),
                        status = "Active"
                    )
                }
            }


    fun getSessionsByUser(userId: Long) =
        sessionDao.getSessionsByUser(userId)

    fun getSessionWithExercises(sessionId: Long) =
        sessionDao.getSessionWithExercises(sessionId)

    fun getAllSessionsWithExercises(userId: Long) =
        sessionDao.getAllSessionsWithExercises(userId)

    /** Saves a full workout plan in one transaction */
    suspend fun saveWorkoutPlan(
        userId : Long,
        plan   : WorkoutPlanData
    ): SavePlanResult {
        return try {

            val startAt = DateTimeUtils.DtToEpochMillis(
                dateStr = plan.date,
                hour = plan.startHour,
                minute = plan.startMin
            )
            val endAt = DateTimeUtils.DtToEpochMillis(
                dateStr = plan.date,
                hour = plan.endHour,
                minute = plan.endMin
            )
            val duration = DateTimeUtils.TimeDurationMinute(
                plan.startHour,
                plan.startMin,
                plan.endHour,
                plan.endMin
            )

            val dupSessionCount = sessionDao.countDuplicateSessions(
                startAt = startAt,
                endAt = endAt
            )

            if (dupSessionCount > 0) {
                return SavePlanResult.Error("Duplicate time slot")
            }

            // 1. Cache exercises locally
            val exerciseEntities = plan.entries.map { it.exercise.toEntity() }
            exerciseDao.insertExercises(exerciseEntities)

            // 2. Insert session
            val sessionId = sessionDao.insertSession(
                SessionEntity(
                    ownerUserId = userId,
                    sessionName = plan.sessionName,
                    dtStartSession = startAt, //hard code
                    dtEndSession = endAt, //hard code
                    duration = duration.toDouble()
                )
            )

            // 3. Insert session exercises
            val sessionExercises = plan.entries.mapIndexed { index, entry ->
                entry.toSessionExerciseEntity(
                    sessionId = sessionId,
                    order = index
                )
            }
            sessionExerciseDao.insertAll(sessionExercises)

            SavePlanResult.Success(sessionId)

        } catch (e: Exception) {
            SavePlanResult.Error(e.message ?: "Failed to save workout session")

        }
    }
    suspend fun deleteSession(sessionId: Long) {
        val session = sessionDao.getSessionById(sessionId) ?: return
        sessionDao.deleteSession(session)
    }

    // -- Exercise cache ----------------------------------------------------------

    fun searchCachedExercises(query: String): Flow<List<ExerciseEntity>> =
        exerciseDao.searchExercises(query)

    suspend fun cacheExercises(exercises: List<Exercise>) {
        exerciseDao.insertExercises(exercises.map { it.toEntity() })
    }
}

// -------------------------------------------------------------------------------------------
// Extension mappers
// -------------------------------------------------------------------------------------------

fun Exercise.toEntity() = ExerciseEntity(
    exerciseId     = id,
    name           = name,
    gifUrl         = gifUrl,
    bodyParts       = bodyParts.joinToString(",")
)

fun WorkoutEntry.toSessionExerciseEntity(
    sessionId : Long,
    order     : Int
) = SessionExerciseEntity(
    ownerSessionId = sessionId,
    exerciseRefId = exercise.id,
    sets = sets,
    reps = reps,
    timeHr = timeHr,
    timeMin = timeMin,
    note = note
)

