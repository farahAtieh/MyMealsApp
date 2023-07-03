package com.example.mymealsapp.android.presentation.main

import android.os.Bundle
import android.widget.Toolbar
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mymealsapp.android.MyApplicationTheme
import com.example.mymealsapp.android.presentation.details.DetailsScreen
import com.example.mymealsapp.android.presentation.details.DetailsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "mainScreen") {
                        composable("mainScreen") {
                            MainScreen(
                                navController
                            )
                        }
                        composable(
                            "mealDetails/{mealId}",
                            arguments = listOf(navArgument("mealId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            DetailsScreen(navController,
                                backStackEntry.arguments?.getString("mealId") ?: ""
                            )
                        }
                    }

                }
            }
        }
    }
}