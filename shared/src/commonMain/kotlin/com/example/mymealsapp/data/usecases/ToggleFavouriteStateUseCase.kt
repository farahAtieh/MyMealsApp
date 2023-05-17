package com.example.mymealsapp.data.usecases

import com.example.mymealsapp.data.Meal
import com.example.mymealsapp.repository.MealsRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ToggleFavouriteStateUseCase : KoinComponent {

    private val mealsRepository: MealsRepository by inject()
    suspend operator fun invoke(meal: Meal){
        mealsRepository.update(meal.copy(isFavourite = !meal.isFavourite))
    }
}