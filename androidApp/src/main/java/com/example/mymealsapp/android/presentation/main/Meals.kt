package com.example.mymealsapp.android.presentation.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mymealsapp.data.Meal
import com.google.accompanist.coil.rememberCoilPainter

@Composable
fun Meals(
    navController: NavController,
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
                            .clip(RoundedCornerShape(10))
                            .clickable {
                                navController.navigate("mealDetails/${meal.id}")
                            },
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
