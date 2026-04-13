package com.example.cs551fitnessapp.database

import androidx.room.Dao

import androidx.room.*
import com.example.cs551fitnessapp.database.ExerciseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {


//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertExercise(exercise: ExerciseEntity)

    // Upsert — insert from API, replace if already cached
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercises(exercises: List<ExerciseEntity>)

    @Delete
    suspend fun deleteExercise(exercise: ExerciseEntity)

    @Query("SELECT * FROM exercises WHERE exerciseId = :id")
    suspend fun getExerciseById(id: String): ExerciseEntity?

    @Query("SELECT * FROM exercises ORDER BY name ASC")
    fun getAllExercises(): Flow<List<ExerciseEntity>>

    // local search on cached exercises
    @Query("""
        SELECT * FROM exercises 
        WHERE name LIKE '%' || :query || '%' 
        ORDER BY name ASC
    """)
    fun searchExercises(query: String): Flow<List<ExerciseEntity>>

    // Clear cache older than given timestamp
    @Query("DELETE FROM exercises WHERE cachedAt < :olderThan")
    suspend fun clearStaleCache(olderThan: Long)
}