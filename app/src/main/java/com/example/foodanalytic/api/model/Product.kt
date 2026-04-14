package com.example.foodanalytic.api.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entité Product représentant un produit alimentaire.
 */
@Entity
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val barcode: String?,
    val name: String,
    val brand: String?,
    val imageUrl: String?,
    val nutriscore: String?,
    val calories: Double?,
    val proteins: Double?,
    val carbohydrates: Double?,
    val fats: Double?,
    val salt: Double?,
    val sugars: Double?
)
