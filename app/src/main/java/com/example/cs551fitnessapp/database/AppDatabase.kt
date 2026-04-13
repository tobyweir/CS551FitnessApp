package com.example.cs551fitnessapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [
        UserEntity::class,
        SessionEntity::class,
        SessionExerciseEntity::class,
        ExerciseEntity::class,
    ],
    version = 1,
    exportSchema = true
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun appointmentDao(): UserAppointmentDao
    abstract fun userDao()            : UserDao
    abstract fun sessionDao()         : SessionDao
    abstract fun exerciseDao()        : ExerciseDao
    abstract fun sessionExerciseDao() : SessionExerciseDao


    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    //.addMigrations(MIGRATION_2_3)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }


}
