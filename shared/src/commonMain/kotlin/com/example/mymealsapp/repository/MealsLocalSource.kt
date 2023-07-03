package com.example.mymealsapp.repository

import com.example.mymealsapp.data.Meal
import com.example.mymealsapp.db.MealsDatabase
import com.example.mymealsapp.util.DispatcherProvider
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class MealsLocalSource(
    database: MealsDatabase,
    private val dispatcherProvider: DispatcherProvider,
) {

    private val dao = database.mealEntityQueries
    private val savedDao = database.savedMealEntityQueries

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
                    isFavourite = it.isFavourite ?: false
                )
            }
        }

    suspend fun selectAll() =
        withContext(dispatcherProvider.io) {
            dao.selectAll { id, name, imageUrl, category, instructions, isFavourite ->
                Meal(id, name, imageUrl, category, instructions, isFavourite = isFavourite ?: false)
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

    suspend fun saveMeal(meal: Meal) {
        withContext(dispatcherProvider.io) {
            savedDao.insert(
                meal.id,
                meal.name,
                meal.imageUrl,
                meal.category,
                meal.instructions,
                isSaved = true
            )
        }
    }

    suspend fun unSaveMeal(mealId: String) {
        withContext(dispatcherProvider.io) {
            savedDao.deleteById(mealId)
        }
    }

    suspend fun getSavedMealById(mealId: String) =
        withContext(dispatcherProvider.io) {
            savedDao.select(mealId) { id, name, imageUrl, category, instructions, isSaved ->
                Meal(id, name, imageUrl, category, instructions, isSaved = isSaved ?: false)
            }.executeAsOneOrNull()
        }

    suspend fun getSavedMeals() =
        withContext(dispatcherProvider.io) {
            savedDao.selectAll() { id, name, imageUrl, category, instructions, isSaved ->
                Meal(id, name, imageUrl, category, instructions, isSaved = isSaved ?: false)
            }.executeAsList()
        }
}