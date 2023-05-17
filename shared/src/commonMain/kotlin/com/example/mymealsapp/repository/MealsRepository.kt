package com.example.mymealsapp.repository

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MealsRepository : KoinComponent {

    private val remoteSource: MealsRemoteSource by inject()

    suspend fun fetch() = remoteSource.getMeals()
}