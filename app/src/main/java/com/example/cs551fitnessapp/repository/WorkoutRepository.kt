package com.example.cs551fitnessapp.repository

import com.example.cs551fitnessapp.database.Exercise
import com.example.cs551fitnessapp.database.ExerciseDao
import com.example.cs551fitnessapp.database.ExerciseEntity
import com.example.cs551fitnessapp.database.SessionDao
import com.example.cs551fitnessapp.database.SessionEntity
import com.example.cs551fitnessapp.database.SessionExerciseDao
import com.example.cs551fitnessapp.database.SessionExerciseEntity
import com.example.cs551fitnessapp.database.WorkoutEntry
import com.example.cs551fitnessapp.database.WorkoutPlanData
import com.example.cs551fitnessapp.database.member.MemberDao
import com.example.cs551fitnessapp.ui.utils.DateTimeUtils
import com.example.cs551fitnessapp.ui.viewmodels.MemberSession
import com.example.cs551fitnessapp.ui.viewmodels.SavePlanResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WorkoutRepository(
    private val sessionDao: SessionDao,
    private val exerciseDao: ExerciseDao,
    private val sessionExerciseDao: SessionExerciseDao,
    private val memberDao: MemberDao
) {

    fun getAllSessionsInRange(startAt: Long, endAt: Long): Flow<List<MemberSession>> =
        combine(
            sessionDao.getAllSessionsInRange(startAt, endAt),
            memberDao.getAllMembers()
        ) { sessions, members ->
            val memberMap = members.associateBy { it.memberId }

            sessions.map { session ->
                val member = memberMap[session.ownerMemberId]

                MemberSession(
                    sessionId = session.sessionId,
                    memberId = session.ownerMemberId,
                    memberName = member?.name ?: "Unknown Member",
                    sessionName = session.sessionName,
                    startTime = formatTime(session.dtStartSession),
                    duration = formatDuration(session.duration),
                    endTime = formatTime(session.dtEndSession)
                )
            }
        }

    fun getSessionsByMember(memberId: Long): Flow<List<SessionEntity>> =
        sessionDao.getSessionsByMember(memberId)

    fun getSessionWithExercises(sessionId: Long) =
        sessionDao.getSessionWithExercises(sessionId)

    fun getAllSessionsWithExercises(memberId: Long) =
        sessionDao.getAllSessionsWithExercises(memberId)

    suspend fun saveWorkoutPlan(
        memberId: Long,
        plan: WorkoutPlanData
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

            val overlappingCount = sessionDao.countOverlappingSessions(
                //memberId = memberId,
                startAt = startAt,
                endAt = endAt
            )

            if (overlappingCount > 0) {
                return SavePlanResult.Error("Duplicate time slot")
            }

            val exerciseEntities = plan.entries.map { it.exercise.toEntity() }
            exerciseDao.insertExercises(exerciseEntities)

            val sessionId = sessionDao.insertSession(
                SessionEntity(
                    ownerMemberId = memberId,
                    sessionName = plan.sessionName,
                    dtStartSession = startAt,
                    dtEndSession = endAt,
                    duration = duration.toLong()
                )
            )

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

    fun searchCachedExercises(query: String): Flow<List<ExerciseEntity>> =
        exerciseDao.searchExercises(query)

    suspend fun cacheExercises(exercises: List<Exercise>) {
        exerciseDao.insertExercises(exercises.map { it.toEntity() })
    }

    private fun formatTime(timestamp: Long): String {
        return SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(timestamp))
    }

    private fun formatDuration(duration: Long): String {
        val totalMinutes = duration.toInt()
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60

        return when {
            hours > 0 && minutes > 0 -> "${hours} hr ${minutes} min"
            hours > 0 -> "${hours} hr"
            else -> "${minutes} min"
        }
    }
}

fun Exercise.toEntity() = ExerciseEntity(
    exerciseId = id,
    name = name,
    gifUrl = gifUrl,
    bodyParts = bodyParts.joinToString(",")
)

fun WorkoutEntry.toSessionExerciseEntity(
    sessionId: Long,
    order: Int
) = SessionExerciseEntity(
    ownerSessionId = sessionId,
    exerciseRefId = exercise.id,
    sets = sets,
    reps = reps,
    timeHr = timeHr,
    timeMin = timeMin,
    note = note
)