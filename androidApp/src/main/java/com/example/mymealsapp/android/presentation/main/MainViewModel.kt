package com.example.mymealsapp.android.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymealsapp.data.Meal
import com.example.mymealsapp.data.usecases.FetchMealsUseCase
import com.example.mymealsapp.data.usecases.GetMealsUseCase
import com.example.mymealsapp.data.usecases.LookUpMealUseCase
import com.example.mymealsapp.data.usecases.ToggleFavouriteStateUseCase
import com.example.mymealsapp.repository.MealsRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainViewModel(
    mealsRepository: MealsRepository,
    private val getMeals: GetMealsUseCase,
    private val fetchMeals: FetchMealsUseCase,
    private val onToggleFavouriteState: ToggleFavouriteStateUseCase,
): ViewModel() {

    private val _state = MutableStateFlow(State.LOADING)
    val state: StateFlow<State> = _state

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private val _events = MutableSharedFlow<Event>()
    val events: SharedFlow<Event> = _events

    private val _shouldFilterFavourites = MutableStateFlow(false)
    val shouldFilterFavourites: StateFlow<Boolean> = _shouldFilterFavourites

    val meals = mealsRepository.meals
        .combine(_shouldFilterFavourites){ meals, shouldFilterFavourites ->
            if(shouldFilterFavourites){
                meals.filter { meal ->
                    meal.isFavourite
                }
            }else {
                meals
            }.also {
                _state.value =
                    if(it.isEmpty()) State.EMPTY else State.NORMAL
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList()
        )

    init {
        loadDate()
    }
    fun loadDate(isForceRefresh: Boolean = false){
        val parameterValue = getRandomChar()
        val getData: suspend () -> List<Meal> = {
            if (isForceRefresh) {
                fetchMeals.invoke(parameterValue)
            } else {
                getMeals.invoke(parameterValue)
            }
        }

        if (isForceRefresh){
            _isRefreshing.value = true
        }else {
            _state.value = State.LOADING
        }

        viewModelScope.launch {
            _state.value = try {
                getData()
                State.NORMAL
            }catch (e: Exception){
                State.ERROR
            }
            _isRefreshing.value = false
        }
    }
    fun refresh(){
        loadDate(true)
    }

    fun onToggleFavouriteFilter(){
        _shouldFilterFavourites.value = !shouldFilterFavourites.value
    }

    fun onFavouriteTapped(meal: Meal){
        viewModelScope.launch {
            try {
                onToggleFavouriteState(meal)
            }catch (e: Exception){
                _events.emit(Event.Error)
            }
        }
    }

    /**
     * Use this method to generate a random number between 65 and 90 (ASCII values for A to Z)
     */
    private fun getRandomChar(): String {
        val asciiValue = Random.nextInt(65, 91)
        return "${asciiValue.toChar()}"
    }
}