package com.example.mymealsapp.api

import com.example.mymealsapp.data.Meal
import io.ktor.client.call.body
import io.ktor.client.request.get

internal class MealsApi(private val ktorApi: KtorApi) : KtorApi by ktorApi {

    suspend fun getMealsByFirstLetter(parameterValue: String): MealResponse = client.get {
        apiUrl("json/v1/1/search.php", GET_MEALS_BY_FIRST_LETTER_PARAM_KEY, parameterValue)
    }.body()

    suspend fun lookUpMealById(parameterValue: String): MealResponse = client.get {
        apiUrl("json/v1/1/lookup.php", LOOK_UP_MEAL_PARAM_KEY, parameterValue)
    }.body()

    companion object {
        private const val GET_MEALS_BY_FIRST_LETTER_PARAM_KEY = "f"
        private const val LOOK_UP_MEAL_PARAM_KEY = "i"
    }
}