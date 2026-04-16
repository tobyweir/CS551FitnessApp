package com.example.cs551fitnessapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cs551fitnessapp.database.member.MemberDao
import com.example.cs551fitnessapp.database.member.MemberEntity

@Database(
    entities = [
        MemberEntity::class,
        UserEntity::class,
        SessionEntity::class,
        ExerciseEntity::class,
        SessionExerciseEntity::class,
        UserAppointmentEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun memberDao(): MemberDao
    abstract fun userDao(): UserDao
    abstract fun sessionDao(): SessionDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun sessionExerciseDao(): SessionExerciseDao
    abstract fun appointmentDao(): UserAppointmentDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fitness_app_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}