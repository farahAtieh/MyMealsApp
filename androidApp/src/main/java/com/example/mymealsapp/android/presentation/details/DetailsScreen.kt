package com.example.mymealsapp.android.presentation.details

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.mymealsapp.android.presentation.main.State
import org.koin.androidx.compose.getViewModel

@Composable
fun DetailsScreen(navController: NavController, mealId: String) {

    val viewModel = getViewModel<DetailsViewModel>()
    val state by viewModel.state.collectAsState()
    viewModel.lookUpMeal(mealId)

    val scaffoldState = rememberScaffoldState()
    val iconResourceId = remember {
        mutableStateOf(Icons.Filled.Favorite)
    }
    Scaffold(scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text(text = "Meal Details") },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "")
                    }
                },
                contentColor = Color.Black
            )
        }) { contentPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            when (state) {
                LoadState.Loading -> {
                    Spacer(modifier = Modifier.weight(1f))
                    CircularProgressIndicator(
                        Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
                is LoadState.Error -> {
                    Log.d("FRH", "error state")
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = "Oops something went wrong...",
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    TextButton(
                        onClick = { viewModel.lookUpMeal(mealId) },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(text = "REFRESH")
                    }
                    Spacer(Modifier.weight(1f))
                }
                is LoadState.Loaded -> {
                    MealDetails(
                        meal = state.getValueOrNull())
                }
            }
        }
    }
}