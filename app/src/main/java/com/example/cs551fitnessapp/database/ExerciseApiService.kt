package com.example.cs551fitnessapp.database


import com.example.cs551fitnessapp.database.ExercisesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ExerciseApiService {

    @GET("api/v1/exercises")
    suspend fun getExercises(
        @Query("name")      query  : String  = "",
        @Query("limit")  limit  : Int     = 20,
        @Query("after")  after  : String? = null,
        @Query("before") before : String? = null
    ): ExercisesResponse
}