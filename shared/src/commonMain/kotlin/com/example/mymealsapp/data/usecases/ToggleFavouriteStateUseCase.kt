package com.example.mymealsapp.data.usecases

import com.example.mymealsapp.data.Meal

class ToggleFavouriteStateUseCase {

    suspend operator fun invoke(meal: Meal){}
}