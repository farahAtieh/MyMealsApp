package com.example.mymealsapp.api

import com.example.mymealsapp.data.Meal
import io.ktor.client.call.body
import io.ktor.client.request.get

internal class MealsApi(private val ktorApi: KtorApi) : KtorApi by ktorApi {

    suspend fun getMeals(): List<Meal> = client.get {
        apiUrl("search.php?s=chicken")
    }.body()
}