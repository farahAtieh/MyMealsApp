package com.example.mymealsapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Meal(
    val idMeal: String,
    val strArea: String,
    val strCategory: String,
    val strInstructions: String,
    val strMeal: String,
    val strMealThumb: String,
    val isFavourite: Boolean = false
)