package com.example.mymealsapp.android.presentation.savedmeals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymealsapp.android.presentation.details.LoadState
import com.example.mymealsapp.android.presentation.main.State
import com.example.mymealsapp.data.Meal
import com.example.mymealsapp.data.usecases.GetSavedMealsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.lang.Exception

class SavedMealViewModel(
    private val getSavedMealsUseCase: GetSavedMealsUseCase,
) : ViewModel() {

    private val _state: MutableStateFlow<LoadState<List<Meal>?>> =
        MutableStateFlow(LoadState.Loading)
    val state: StateFlow<LoadState<List<Meal>?>> = _state

    init {
        getSavedMeals()
    }

    fun getSavedMeals() = viewModelScope.launch {
        viewModelScope.launch {
            try {
                val data = getSavedMealsUseCase.invoke()
                _state.value = LoadState.Loaded(data)
            } catch (e: Exception) {
                _state.value =
                    LoadState.Error(e)
            }
        }
    }
}