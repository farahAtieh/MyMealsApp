package com.example.mymealsapp.data.usecases

import com.example.mymealsapp.data.Meal
import com.example.mymealsapp.repository.MealsRepository
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetMealsUseCase : KoinComponent{
    private val mealsRepository: MealsRepository by inject()
    @NativeCoroutines
    suspend fun invoke(parameterValue: String): List<Meal> = mealsRepository.get(parameterValue)
}