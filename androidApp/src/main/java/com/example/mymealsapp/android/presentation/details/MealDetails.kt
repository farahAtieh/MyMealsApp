package com.example.mymealsapp.android.presentation.details

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.mymealsapp.android.ui.convertToParagraph
import com.example.mymealsapp.android.ui.getTitleBold
import com.example.mymealsapp.data.Meal
import com.google.accompanist.coil.rememberCoilPainter
import org.koin.androidx.compose.getViewModel

@Composable
fun MealDetails(
    meal: Meal?,
) {
    val viewModel = getViewModel<DetailsViewModel>()
    val savedState by viewModel.savedState.collectAsState()

    when (savedState) {
        is LoadState.Loading -> {}

        is LoadState.Error -> {
            Toast.makeText(
                LocalContext.current,
                "Item ${meal?.name} UnSaved Successfully!",
                Toast.LENGTH_SHORT
            ).show()
        }

        is LoadState.Loaded -> {
            Toast.makeText(
                LocalContext.current,
                "Item ${meal?.name} ${if (savedState.getValueOrNull() == true) "saved" else "unsaved"} Successfully!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    Column(
        Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        meal?.let {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .verticalScroll(ScrollState(0))
            ) {
                Row {
                    Text(
                        text = meal.name,
                        overflow = TextOverflow.Ellipsis,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(10.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = {
                        viewModel.onSaveBtnClicked(meal)
                    }) {
                        Icon(
                            if (meal.isSaved) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = ""
                        )
                    }
                }

                Image(
                    painter = rememberCoilPainter(
                        request = meal.imageUrl
                    ),
                    contentDescription = "${meal.name}-image",
                    modifier = Modifier
                        .aspectRatio(1f)
                        .fillMaxSize()
                        .align(Alignment.CenterHorizontally)
                        .clip(RoundedCornerShape(2)),
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = "Category:".getTitleBold(meal.category),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.padding(8.dp)
                )

                Text(
                    text = "Instructions:\n",
                    overflow = TextOverflow.Ellipsis,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(8.dp)
                )
                Text(
                    text = meal.instructions.convertToParagraph(),
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = 8.dp, start = 8.dp, end = 8.dp)
                )
            }
        }
    }
}

