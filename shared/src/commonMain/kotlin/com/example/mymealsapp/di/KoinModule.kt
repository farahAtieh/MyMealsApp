package com.example.mymealsapp.di

import com.example.mymealsapp.api.KtorApi
import com.example.mymealsapp.api.KtorApiImpl
import com.example.mymealsapp.api.MealsApi
import com.example.mymealsapp.data.usecases.FetchMealsUseCase
import com.example.mymealsapp.data.usecases.GetMealsUseCase
import com.example.mymealsapp.data.usecases.GetSavedMealsUseCase
import com.example.mymealsapp.data.usecases.LookUpMealByIdUseCase
import com.example.mymealsapp.data.usecases.SaveMealUseCase
import com.example.mymealsapp.data.usecases.ToggleFavouriteStateUseCase
import com.example.mymealsapp.data.usecases.UnSaveMealUseCase
import com.example.mymealsapp.db.MealsDatabase
import com.example.mymealsapp.repository.MealsLocalSource
import com.example.mymealsapp.repository.MealsRemoteSource
import com.example.mymealsapp.repository.MealsRepository
import com.example.mymealsapp.util.getDispatcherProvider
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(sharedModules)
    }

//called by IOS
fun initKoin() = initKoin {}

private val utilityModule = module {
    factory { getDispatcherProvider() }
    single { MealsDatabase(get()) }
}
private val apiModule = module {
    single<KtorApi> { KtorApiImpl }
    factory { MealsApi(get()) }
}

private val repositoryModule = module {
    factory { MealsRemoteSource(get(), get()) }
    factory { MealsLocalSource(get(), get()) }
    factory { MealsRepository() }
}

private val usecasesModule = module {
    factory { GetMealsUseCase() }
    factory { FetchMealsUseCase() }
    factory { ToggleFavouriteStateUseCase() }
    factory { LookUpMealByIdUseCase() }
    factory { SaveMealUseCase() }
    factory { UnSaveMealUseCase() }
    factory { GetSavedMealsUseCase() }
}

private val sharedModules = listOf(utilityModule, apiModule, repositoryModule, usecasesModule, platformModule)

