package com.example.mymealsapp.android.presentation.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.mymealsapp.android.MyApplicationTheme
import com.example.mymealsapp.data.Meal
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.example.mymealsapp.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen(viewModel = remember {
                        MainViewModel()
                    })
                }
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val state by viewModel.state.collectAsState()
    val meals by viewModel.meals.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val shouldFilterFavourites by viewModel.shouldFilterFavourites.collectAsState()

    val scaffoldState = rememberScaffoldState()

    Scaffold(scaffoldState = scaffoldState) { contentPadding ->
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
                    MainViewModel.State.LOADING -> {
                        Spacer(modifier = Modifier.weight(1f))
                        CircularProgressIndicator(
                            Modifier.align(Alignment.CenterHorizontally)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }

                    MainViewModel.State.NORMAL -> Meals(
                        meals = meals,
                        onFavouriteTapped = viewModel::onFavouriteTapped
                    )

                    MainViewModel.State.ERROR -> {
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

                    MainViewModel.State.EMPTY -> {
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

@Composable
fun Meals(
    meals: List<Meal>,
    onFavouriteTapped: (Meal) -> Unit = {},
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        content = {
            items(meals) { meal ->
                Column(Modifier.padding(8.dp)) {
                    Image(
                        painter = rememberCoilPainter(
                            request = meal.imageUrl
                        ),
                        contentDescription = "${meal.name}-image",
                        modifier = Modifier
                            .aspectRatio(1f)
                            .fillMaxSize()
                            .align(Alignment.CenterHorizontally)
                            .clip(RoundedCornerShape(10)),
                        contentScale = ContentScale.Crop
                    )
                    Row(
                        Modifier
                            .padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = meal.name,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            if (meal.isFavourite)
                                Icons.Filled.Favorite
                            else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Mark as favourite",
                            modifier = Modifier.clickable {
                                onFavouriteTapped(meal)
                            }
                        )
                    }
                }
            }
        })
}
