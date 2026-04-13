package com.example.foodanalytic.api.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.foodanalytic.api.model.Product
import com.example.foodanalytic.api.model.User


/**
 * ÉTAPE 3 : LA DATABASE (Le point d'accès)
 * C'est le centre de contrôle qui fait le lien entre les entités et le DAO.
 */
@Database(entities = [User::class, Product::class], version = 2)
abstract class AppDb : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao
}
