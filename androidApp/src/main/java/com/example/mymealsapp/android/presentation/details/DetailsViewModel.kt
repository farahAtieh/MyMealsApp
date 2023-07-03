package com.example.mymealsapp.android.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymealsapp.data.Meal
import com.example.mymealsapp.data.usecases.LookUpMealByIdUseCase
import com.example.mymealsapp.data.usecases.SaveMealUseCase
import com.example.mymealsapp.data.usecases.UnSaveMealUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Exception

class DetailsViewModel(
    private val lookUpMealUseCase: LookUpMealByIdUseCase,
    private val saveMealUseCase: SaveMealUseCase,
    private val unSaveMealUseCase: UnSaveMealUseCase,
) : ViewModel() {

    private val _state: MutableStateFlow<LoadState<Meal?>> =
        MutableStateFlow(LoadState.Loading)
    val state: StateFlow<LoadState<Meal?>> = _state

    private val _savedState: MutableStateFlow<LoadState<Boolean>> =
        MutableStateFlow(LoadState.Loading)
    val savedState: StateFlow<LoadState<Boolean>> = _savedState

    fun lookUpMeal(mealId: String) {
        val getData: suspend () -> Meal? = {
            lookUpMealUseCase.invoke(mealId)
        }
        viewModelScope.launch {
            try {
                _state.value =
                    LoadState.Loaded(getData())
            } catch (e: Exception) {
                _state.value =
                    LoadState.Error(e)
            }
        }
    }

    fun onSaveBtnClicked(meal: Meal){
        if(!meal.isSaved){
            saveMeal(meal)
            _state.value = LoadState.Loaded(meal.copy(isSaved = true))
        }else {
            unSaveMeal(mealId = meal.id)
            _state.value = LoadState.Loaded(meal.copy(isSaved = false))
        }
    }
    private fun saveMeal(meal: Meal) {
        viewModelScope.launch {
            try {
                saveMealUseCase.invoke(meal)
                _savedState.value = LoadState.Loaded(true)
            } catch (e: Exception) {
                _savedState.value = LoadState.Error(Throwable())
            }
        }
    }

    private fun unSaveMeal(mealId: String) {
        viewModelScope.launch {
            try {
                unSaveMealUseCase.invoke(mealId)
                _savedState.value = LoadState.Loaded(false)
            } catch (e: Exception) {
                _savedState.value = LoadState.Error(Throwable())
            }
        }
    }
}