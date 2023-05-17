package com.example.mymealsapp.di

import com.example.mymealsapp.data.usecases.FetchMealsUseCase
import com.example.mymealsapp.data.usecases.GetMealsUseCase
import com.example.mymealsapp.data.usecases.ToggleFavouriteStateUseCase
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

private val usecasesModule = module {
    factory { GetMealsUseCase() }
    factory { FetchMealsUseCase() }
    factory { ToggleFavouriteStateUseCase() }
}

private val sharedModules = listOf(usecasesModule)

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin{
        appDeclaration()
        modules(sharedModules)
    }