package com.example.cs551fitnessapp.database


import com.google.gson.annotations.SerializedName


data class ExercisesResponse(
    @SerializedName("data") val data : List<Exercise>,
    @SerializedName("meta") val meta : PaginationMeta? = null
)

data class PaginationMeta(
    @SerializedName("nextCursor") val nextCursor : String? = null,
    @SerializedName("prevCursor") val prevCursor : String? = null,
    @SerializedName("total")      val total      : Int?    = null
)

data class Exercise(
    @SerializedName("exerciseId")     val id             : String,
    @SerializedName("name")           val name           : String,
    @SerializedName("gifUrl")         val gifUrl         : String?,
    @SerializedName("bodyParts")      val bodyParts      : List<String> = emptyList()

)
