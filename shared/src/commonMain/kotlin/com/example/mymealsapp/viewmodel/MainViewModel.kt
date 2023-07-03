package com.example.mymealsapp.viewmodel

import com.example.mymealsapp.data.Meal
import com.example.mymealsapp.data.usecases.FetchMealsUseCase
import com.example.mymealsapp.data.usecases.GetMealsUseCase
import com.example.mymealsapp.data.usecases.ToggleFavouriteStateUseCase
import com.example.mymealsapp.repository.MealsRepository
import com.example.mymealsapp.shared.ui.CoroutineViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.random.Random

class MainViewModel : CoroutineViewModel(), KoinComponent {

    private val mealsRepository: MealsRepository by inject()
    private val getMeals: GetMealsUseCase by inject()
    private val fetchMeals: FetchMealsUseCase by inject()
    private val onToggleFavouriteState: ToggleFavouriteStateUseCase by inject()

    private val _state = MutableStateFlow(State.LOADING)
    val state: StateFlow<State> = _state

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private val _shouldFilterFavourites = MutableStateFlow(false)
    val shouldFilterFavourites: StateFlow<Boolean> = _shouldFilterFavourites

    val meals = mealsRepository.meals
        .combine(_shouldFilterFavourites) { meals, shouldFilterFavourites ->
            if (shouldFilterFavourites) {
                meals.filter { meal ->
                    meal.isFavourite
                }
            } else {
                meals
            }.also {
                _state.value =
                    if (it.isEmpty()) State.EMPTY else State.NORMAL
            }
        }.stateIn(
            coroutineScope,
            SharingStarted.WhileSubscribed(),
            emptyList()
        )

    init {
        loadDate()
    }

    fun loadDate(isForceRefresh: Boolean = false) {
        val parameterValue = getRandomChar()
        val getData: suspend () -> List<Meal> = {
            if (isForceRefresh) {
                fetchMeals.invoke(parameterValue)
            } else {
                getMeals.invoke(parameterValue)
            }
        }

        if (isForceRefresh) {
            _isRefreshing.value = true
        } else {
            _state.value = State.LOADING
        }

        coroutineScope.launch {
            _state.value = try {
                getData()
                State.NORMAL
            } catch (e: Exception) {
                State.ERROR
            }
            _isRefreshing.value = false
        }
    }

    fun refresh() {
        loadDate(true)
    }

    fun onToggleFavouriteFilter() {
        _shouldFilterFavourites.value = ! shouldFilterFavourites.value
    }

    fun onFavouriteTapped(meal: Meal) {
        coroutineScope.launch {
            try {
                onToggleFavouriteState(meal)
            } catch (e: Exception) {
                _state.emit(State.ERROR)
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

    @Suppress("unused")
    fun observeMeals(onChange: (List<Meal>) -> Unit){
        meals.onEach {
            onChange(it)
        }.launchIn(coroutineScope)
    }

    @Suppress("unused")
    fun observeState(onChange: (State) -> Unit){
        state.onEach {
            onChange(it)
        }.launchIn(coroutineScope)
    }

    enum class State {
        LOADING,
        NORMAL,
        ERROR,
        EMPTY
    }
}