package com.example.mymealsapp.repository

import com.example.mymealsapp.api.MealsApi
import com.example.mymealsapp.util.DispatcherProvider
import kotlinx.coroutines.withContext

internal class MealsRemoteSource(
    private val mealsApi: MealsApi,
    private val dispatcherProvider: DispatcherProvider,
) {

    suspend fun getMealsByFirstLetter(parameterValue: String) = withContext(dispatcherProvider.io) {
        mealsApi.getMealsByFirstLetter(parameterValue).meals ?: arrayListOf()
    }

    suspend fun lookUpMeal(mealId: String) =
        withContext(dispatcherProvider.io) {
            mealsApi.lookUpMeal(mealId).meals?.get(0)
        }
}