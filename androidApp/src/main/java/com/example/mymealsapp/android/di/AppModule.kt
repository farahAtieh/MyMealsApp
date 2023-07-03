package com.example.mymealsapp.android.di

import com.example.mymealsapp.android.presentation.details.DetailsViewModel
import com.example.mymealsapp.android.presentation.main.MainViewModel
import com.example.mymealsapp.android.presentation.savedmeals.SavedMealViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel(get(), get(), get(), get()) }
    viewModel { DetailsViewModel(get(), get(), get()) }
    viewModel { SavedMealViewModel(get()) }
}