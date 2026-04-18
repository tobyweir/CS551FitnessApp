package com.example.cs551fitnessapp.database

import androidx.room.*
import com.example.cs551fitnessapp.database.UserEntity
//import com.example.workout.data.db.relation.UserWithSessions
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    @Update
    suspend fun updateUser(user: UserEntity)

    @Delete
    suspend fun deleteUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE userId = :id")
    suspend fun getUserById(id: Int): UserEntity?

    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<UserEntity>>

    // Returns user + all their sessions
    @Transaction
    @Query("SELECT * FROM users WHERE userId = :userId")
    fun getUserWithSessions(userId: Long): Flow<UserWithSessions>
}