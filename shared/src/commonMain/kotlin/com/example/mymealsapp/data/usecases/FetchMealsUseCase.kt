package com.example.mymealsapp.data.usecases

import com.example.mymealsapp.data.Meal
import com.example.mymealsapp.repository.MealsRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FetchMealsUseCase : KoinComponent {

    private val breedsRepository: MealsRepository by inject()
    suspend fun invoke(parameterValue: String): List<Meal> = breedsRepository.fetch(parameterValue)
}