package com.example.mymealsapp.repository

import com.example.mymealsapp.api.MealsApi
import com.example.mymealsapp.util.DispatcherProvider
import kotlinx.coroutines.withContext

internal class MealsRemoteSource(
    private val mealsApi: MealsApi,
    private val dispatcherProvider: DispatcherProvider,
) {

    suspend fun getMeals() = withContext(dispatcherProvider.io) {
        mealsApi.getMeals()
    }
}