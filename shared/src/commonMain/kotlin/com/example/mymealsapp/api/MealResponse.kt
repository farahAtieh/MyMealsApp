package com.example.mymealsapp.api

import com.example.mymealsapp.data.Meal
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MealResponse(
    @SerialName("meals")
    val meals: List<Meal>?
)