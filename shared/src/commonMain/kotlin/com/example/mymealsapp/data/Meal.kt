package com.example.mymealsapp.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Meal(
    @SerialName("idMeal")
    val id: String,
    @SerialName("strMeal")
    val name: String,
    @SerialName("strMealThumb")
    val imageUrl: String,
    @SerialName("strCategory")
    val category: String,
    @SerialName("strInstructions")
    val instructions: String? = "",
    val isFavourite: Boolean = false,
    val isSaved: Boolean = false
)