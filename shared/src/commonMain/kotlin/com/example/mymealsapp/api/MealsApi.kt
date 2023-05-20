package com.example.mymealsapp.api

import io.ktor.client.call.body
import io.ktor.client.request.get

internal class MealsApi(private val ktorApi: KtorApi) : KtorApi by ktorApi {

    suspend fun getMealsByFirstLetter(parameterValue: String): MealResponse = client.get {
        apiUrl("json/v1/1/search.php", parameterValue)
    }.body()
}