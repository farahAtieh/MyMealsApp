package com.example.mymealsapp.android.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymealsapp.data.Meal
import com.example.mymealsapp.data.usecases.LookUpMealUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Exception

class DetailsViewModel(
    private val lookUpMealUseCase: LookUpMealUseCase,
) : ViewModel() {

    private val _state: MutableStateFlow<LoadState<Meal?>> =
        MutableStateFlow(LoadState.Loading)
    val state: StateFlow<LoadState<Meal?>> = _state

    fun lookUpMeal(mealId: String) {
        val getData: suspend () -> Meal? = {
            lookUpMealUseCase.invoke(mealId)
        }
        viewModelScope.launch {
            try {
                _state.value =
                    LoadState.Loaded(getData())
            }catch (e: Exception){
                _state.value =
                    LoadState.Error(e)
            }
        }
    }
}