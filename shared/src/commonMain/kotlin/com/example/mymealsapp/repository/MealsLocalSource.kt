package com.example.mymealsapp.repository

import com.example.mymealsapp.data.Meal
import com.example.mymealsapp.db.MealsDatabase
import com.example.mymealsapp.shared.util.DispatcherProvider
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class MealsLocalSource(
    database: MealsDatabase,
    private val dispatcherProvider: DispatcherProvider,
) {

    private val dao = database.mealsQueries
    val meals = dao.selectAll()
        .asFlow()
        .mapToList()
        .map { meals ->
            meals.map {
                Meal(
                    it.id,
                    it.name,
                    it.imageUrl,
                    it.category,
                    it.instructions,
                    it.isFavourite ?: false
                )
            }
        }

    suspend fun selectAll() =
        withContext(dispatcherProvider.io) {
            dao.selectAll { id, name, imageUrl, category, instructions, isFavourite ->
                Meal(id, name, imageUrl, category, instructions, isFavourite ?: false)
            }.executeAsList()
        }

    suspend fun insert(meal: Meal) =
        withContext(dispatcherProvider.io) {
            dao.insert(
                meal.id,
                meal.name,
                meal.imageUrl,
                meal.category,
                meal.instructions,
                meal.isFavourite
            )
        }

    suspend fun update(meal: Meal) =
        withContext(dispatcherProvider.io) {
            dao.update(meal.isFavourite, meal.id)
        }

    suspend fun clear() =
        withContext(dispatcherProvider.io) {
            dao.clear()
        }
}