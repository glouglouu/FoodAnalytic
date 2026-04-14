package com.example.foodanalytic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.room.Room
import com.example.foodanalytic.api.data.AppDb
import com.example.foodanalytic.api.data.ProductDao
import com.example.foodanalytic.api.data.UserDao
import com.example.foodanalytic.ui.screens.FavoritesScreen
import com.example.foodanalytic.ui.screens.HomeScreen
import com.example.foodanalytic.ui.screens.ProductDetailScreen
import com.example.foodanalytic.ui.screens.ProfileScreen
import com.example.foodanalytic.ui.theme.FoodAnalyticTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = Room.databaseBuilder(
            applicationContext,
            AppDb::class.java, "db"
        )
            .fallbackToDestructiveMigration()
            .build()

        val userDao = db.userDao()
        val productDao = db.productDao()

        setContent {
            FoodAnalyticTheme {
                FoodAnalyticApp(userDao, productDao)
            }
        }
    }
}

@Composable
fun FoodAnalyticApp(userDao: UserDao, productDao: ProductDao) {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }
    var selectedProductId by rememberSaveable { mutableIntStateOf(-1) }

    if (selectedProductId != -1) {
        ProductDetailScreen(
            productId = selectedProductId,
            productDao = productDao,
            onBackClick = { selectedProductId = -1 }
        )
    } else {
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
                AppDestinations.HOME -> HomeScreen(productDao, onProductClick = { selectedProductId = it })
                AppDestinations.FAVORITES -> FavoritesScreen(productDao, onProductClick = { selectedProductId = it })
                AppDestinations.PROFILE -> ProfileScreen()
            }
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
