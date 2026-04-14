package com.example.foodanalytic.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.foodanalytic.api.data.UserDao
import com.example.foodanalytic.api.model.User
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(userDao: UserDao, onLoginSuccess: (User) -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Connexion", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(32.dp))

        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Nom d'utilisateur") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Mot de passe") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                scope.launch {
                    val user = userDao.getUserByUsername(username)
                    if (user != null && user.password == password) {
                        onLoginSuccess(user)
                    } else {
                        Toast.makeText(context, "Identifiants incorrects", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Se connecter")
        }

        TextButton(onClick = {
            scope.launch {
                if (username.isNotEmpty() && password.isNotEmpty()) {
                    val existing = userDao.getUserByUsername(username)
                    if (existing == null) {
                        val newUser = User(username = username, password = password)
                        userDao.insert(newUser)
                        Toast.makeText(context, "Compte créé !", Toast.LENGTH_SHORT).show()
                        val createdUser = userDao.getUserByUsername(username)
                        if (createdUser != null) onLoginSuccess(createdUser)
                    } else {
                        Toast.makeText(context, "Cet utilisateur existe déjà", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
                }
            }
        }) {
            Text("Créer un compte")
        }
    }
}
