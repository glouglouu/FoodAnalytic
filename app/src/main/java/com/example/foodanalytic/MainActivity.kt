package com.example.foodanalytic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.room.Room
import com.example.foodanalytic.api.schema.AppDb
import com.example.foodanalytic.api.schema.User
import com.example.foodanalytic.ui.screens.FavoritesScreen
import com.example.foodanalytic.ui.screens.HomeScreen
import com.example.foodanalytic.ui.screens.ProfileScreen
import com.example.foodanalytic.ui.theme.FoodAnalyticTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        /**
         * INITIALISATION DE LA BASE DE DONNÉES
         * databaseBuilder : Crée l'instance de la base "db" sur le stockage du
        téléphone.
         * .build() : Finalise la création.
         */
        val db = Room.databaseBuilder(
            applicationContext, AppDb::class.java,
            "db"
        ).build()
        val dao = db.dao() // On récupère l'accès aux commandes SQL (DAO)

        setContent {
            /**
             * OBSERVATION DES DONNÉES (Le "Tuyau")
             * collectAsState : Transforme le Flow du DAO en un état lisible par
            Compose.
             * Dès que Room change, la variable 'tasks' est mise à jour et l'écran
            se rafraîchit.
             */
            val users by dao.getAll().collectAsState(initial = emptyList())

            /**
             * GESTION DE L'ASYNCHRONE ET DE L'ÉTAT LOCAL
             * scope : Permet de lancer des actions vers Room (insert/delete) en
            arrière-plan.
             * text : Mémorise ce que l'utilisateur écrit dans le champ de saisie.
             */
            val scope = rememberCoroutineScope()
            var text by remember { mutableStateOf("") }

            Row {
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Faire...") }
                )

                Button(onClick = {
                    // On lance une Coroutine pour insérer sans figer l'écran
                    scope.launch {
                        if (text.isNotBlank()) {
                            dao.insert(User(username = text))
                            text = "" // On vide le champ après l'ajout
                        }
                    }
                }) {
                    Text("OK")
                }
            }
            // LISTE DYNAMIQUE
            LazyColumn {
                // Pour chaque tâche dans la liste 'tasks'
                items(users) { user ->
                    // Un bouton qui affiche le titre et supprime au clic
                    TextButton(onClick = {
                        scope.launch { dao.delete(user) }
                    }) {
                        Text("${user.username} (Supprimer)")
                    }
                }
            }
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
