package com.example.foodanalytic.api.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.foodanalytic.api.model.User


/**
 * ÉTAPE 3 : LA DATABASE (Le point d'accès)
 * C'est le centre de contrôle qui fait le lien entre les entités et le DAO.
 */
@Database(entities = [User::class], version = 1)
abstract class AppDb : RoomDatabase() {
    // Cette fonction permet de récupérer les commandes du DAO.
    abstract fun dao(): UserDao
}