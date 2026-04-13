package com.example.foodanalytic.api.repository

import com.example.foodanalytic.api.data.UserDao
import com.example.foodanalytic.api.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Repository pour la gestion des utilisateurs (Authentification).
 * Équivalent d'un AuthService/UserService en NestJS.
 */
class UserRepository(private val userDao: UserDao) {

    val allUsers: Flow<List<User>> = userDao.getAll()

    /**
     * Enregistre un nouvel utilisateur.
     * @return true si l'inscription est réussie, false si l'utilisateur existe déjà.
     */
    suspend fun register(username: String, password: String): Boolean {
        val existingUser = userDao.getUserByUsername(username)
        if (existingUser != null) {
            return false // Utilisateur déjà existant
        }
        
        val newUser = User(username = username, password = password)
        userDao.insert(newUser)
        return true
    }

    /**
     * Connecte un utilisateur.
     * @return L'objet User si les identifiants sont corrects, sinon null.
     */
    suspend fun login(username: String, password: String): User? {
        val user = userDao.getUserByUsername(username)
        return if (user != null && user.password == password) {
            user
        } else {
            null
        }
    }
}
