package com.example.mymealsapp.repository

import com.example.mymealsapp.data.Meal
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.supervisorScope
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MealsRepository : KoinComponent {

    private val remoteSource: MealsRemoteSource by inject()
    private val localSource: MealsLocalSource by inject()

    @NativeCoroutinesState
    val meals = localSource.meals
    internal suspend fun get(parameterValue: String) =
        with(localSource.selectAll()) {
            if (isNullOrEmpty()) {
                return@with fetch(parameterValue)
            } else {
                this
            }
        }

    internal suspend fun fetch(parameterValue: String) = supervisorScope {
        remoteSource.getMealsByFirstLetter(parameterValue)
            .also { meals ->
                localSource.clear()
                meals.map { meal ->
                    async {
                        localSource.insert(meal)
                    }
                }.awaitAll()
            }
    }

    suspend fun update(meal: Meal) =
        localSource.update(meal)
}