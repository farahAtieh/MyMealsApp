package com.example.mymealsapp.repository

import com.example.mymealsapp.data.Meal
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.supervisorScope
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MealsRepository : KoinComponent {

    private val remoteSource: MealsRemoteSource by inject()
    private val localSource: MealsLocalSource by inject()

    val meals = localSource.meals
    internal suspend fun get() =
        with(localSource.selectAll()) {
            if (isNullOrEmpty()) {
                return@with fetch()
            } else {
                this
            }
        }

    internal suspend fun fetch() = supervisorScope {
        remoteSource.getMeals()
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