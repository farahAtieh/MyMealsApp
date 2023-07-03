package com.example.mymealsapp.android.presentation.savedmeals

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mymealsapp.android.presentation.details.LoadState
import com.example.mymealsapp.data.Meal
import com.google.accompanist.coil.rememberCoilPainter
import org.koin.androidx.compose.getViewModel

@Composable
fun SavedScreen(navController: NavController) {

    val viewModel = getViewModel<SavedMealViewModel>()
    val state by viewModel.state.collectAsState()

    val scaffoldState = rememberScaffoldState()

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
                        onClick = { viewModel.getSavedMeals() },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(text = "REFRESH")
                    }
                    Spacer(Modifier.weight(1f))
                }

                is LoadState.Loaded -> {
                    SavedMeals(
                        meals = state.getValueOrNull() !!
                    )
                }
            }
        }
    }
}

@Composable
fun SavedMeals(meals: List<Meal>) {
    LazyColumn {
        items(meals) { meal ->
            Row(
                Modifier
                    .padding(vertical = 8.dp)
            ) {
                Image(
                    painter = rememberCoilPainter(
                        request = meal.imageUrl
                    ),
                    contentDescription = "${meal.name}-image",
                    modifier = Modifier
                        .padding(start = 6.dp, top = 10.dp)
                        .size(100.dp)
                        .clip(RoundedCornerShape(100)),
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = meal.name,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.padding(start = 6.dp, top = 20.dp)
                )

            }

            Divider(color = Color.LightGray)

        }
    }
}
