package com.example.cs551fitnessapp.database

import android.content.Context
import com.example.cs551fitnessapp.repository.MemberRepository
import com.example.cs551fitnessapp.repository.WorkoutRepository

object DatabaseModule {

    fun provideRepository(context: Context): WorkoutRepository {
        val db = AppDatabase.getDatabase(context)
        return WorkoutRepository(
            sessionDao = db.sessionDao(),
            exerciseDao = db.exerciseDao(),
            sessionExerciseDao = db.sessionExerciseDao()
        )
    }

    fun provideMemberRepository(context: Context): MemberRepository {
        val db = AppDatabase.getDatabase(context)
        return MemberRepository(db.memberDao())
    }
}