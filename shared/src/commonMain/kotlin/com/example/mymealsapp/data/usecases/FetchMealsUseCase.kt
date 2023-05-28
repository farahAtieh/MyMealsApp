package com.example.mymealsapp.data.usecases

import com.example.mymealsapp.data.Meal
import com.example.mymealsapp.repository.MealsRepository
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.native.ObjCName

class FetchMealsUseCase : KoinComponent {

    private val breedsRepository: MealsRepository by inject()
    @NativeCoroutines
    @ObjCName(name = "invokeNative")
    suspend fun invoke(parameterValue: String): List<Meal> = breedsRepository.fetch(parameterValue)
}