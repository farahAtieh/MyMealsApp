package com.example.mymealsapp.android.presentation.main

import android.graphics.drawable.Icon
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.mymealsapp.android.presentation.details.DetailsViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import org.koin.androidx.compose.getViewModel

@Composable
fun MainScreen(navController: NavController) {
    val viewModel = getViewModel<MainViewModel>()
    val state by viewModel.state.collectAsState()
    val meals by viewModel.meals.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val shouldFilterFavourites by viewModel.shouldFilterFavourites.collectAsState()

    val scaffoldState = rememberScaffoldState()
    Scaffold(scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text(text = "Meals") },
                contentColor = Color.Black)
        }) { contentPadding ->
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
            onRefresh = viewModel::refresh,
            modifier = Modifier.padding(contentPadding)
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                Row(
                    Modifier
                        .wrapContentWidth(Alignment.End)
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(text = "Filter favourite")
                    Switch(checked = shouldFilterFavourites,
                        modifier = Modifier.padding(horizontal = 8.dp),
                        onCheckedChange = {
                            viewModel.onToggleFavouriteFilter()
                        })

                }
                when (state) {
                    State.LOADING -> {
                        Spacer(modifier = Modifier.weight(1f))
                        CircularProgressIndicator(
                            Modifier.align(Alignment.CenterHorizontally)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }

                    State.NORMAL -> Meals(
                        navController,
                        meals = meals,
                        onFavouriteTapped = viewModel::onFavouriteTapped
                    )

                    State.ERROR -> {
                        Log.d("FRH", "error state")
                        Spacer(Modifier.weight(1f))
                        Text(
                            text = "Oops something went wrong...",
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        TextButton(
                            onClick = viewModel::refresh,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text(text = "REFRESH")
                        }
                        Spacer(Modifier.weight(1f))
                    }

                    State.EMPTY -> {
                        Log.d("FRH", "empty state")
                        Spacer(Modifier.weight(1f))
                        Text(
                            text = "Oops looks like there are no ${if (shouldFilterFavourites) "favourites" else "meals"}",
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        TextButton(
                            onClick = viewModel::refresh,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text(text = "REFRESH")
                        }
                        Spacer(Modifier.weight(1f))
                    }
                }
            }
        }
    }
}