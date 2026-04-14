package com.example.foodanalytic.api.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.foodanalytic.api.model.Product
import com.example.foodanalytic.api.model.User

/**
 * Version augmentée à 3 pour forcer la migration suite à l'ajout de champs dans Product
 */
@Database(entities = [User::class, Product::class], version = 3)
abstract class AppDb : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao
}
