package com.example.foodanalytic.api.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.foodanalytic.api.model.Product
import com.example.foodanalytic.api.model.User

/**
 * Version 4 suite à l'ajout des URLs d'images supplémentaires
 */
@Database(entities = [User::class, Product::class], version = 4)
abstract class AppDb : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao
}
