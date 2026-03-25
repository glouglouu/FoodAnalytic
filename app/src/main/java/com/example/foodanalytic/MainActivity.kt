package com.example.foodanalytic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.example.foodanalytic.ui.screens.FavoritesScreen
import com.example.foodanalytic.ui.screens.HomeScreen
import com.example.foodanalytic.ui.screens.ProfileScreen
import com.example.foodanalytic.ui.theme.FoodAnalyticTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FoodAnalyticTheme {
                FoodAnalyticApp()
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun FoodAnalyticApp() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = {
                        Icon(
                            it.icon,
                            contentDescription = it.label
                        )
                    },
                    label = { Text(it.label) },
                    selected = it == currentDestination,
                    onClick = { currentDestination = it }
                )
            }
        }
    ) {
        when (currentDestination) {
            AppDestinations.HOME -> HomeScreen()
            AppDestinations.FAVORITES -> FavoritesScreen()
            AppDestinations.PROFILE -> ProfileScreen()
        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
) {
    HOME("Accueil", Icons.Default.Home),
    FAVORITES("Favoris", Icons.Default.Favorite),
    PROFILE("Profil", Icons.Default.AccountBox),
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FoodAnalyticTheme {
        FoodAnalyticApp()
    }
}
