package com.example.foodanalytic.api.network

import com.example.foodanalytic.api.model.Product
import com.google.gson.annotations.SerializedName

data class BarcodeResponse(
    val code: String?,
    val product: ApiProduct?,
    val status: Int?,
    @SerializedName("status_verbose") val statusVerbose: String?
)

data class ApiProduct(
    @SerializedName("product_name") val productName: String?,
    val brands: String?,
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("image_front_url") val imageFrontUrl: String?,
    @SerializedName("image_ingredients_url") val imageIngredientsUrl: String?,
    @SerializedName("image_nutrition_url") val imageNutritionUrl: String?,
    @SerializedName("image_packaging_url") val imagePackagingUrl: String?,
    @SerializedName("nutriscore_grade") val nutriscoreGrade: String?,
    val nutriments: Nutriments?
) {
    fun toProduct(barcode: String?): Product {
        return Product(
            barcode = barcode,
            name = productName ?: "Produit inconnu",
            brand = brands,
            imageUrl = imageUrl,
            frontImageUrl = imageFrontUrl,
            ingredientsImageUrl = imageIngredientsUrl,
            nutritionImageUrl = imageNutritionUrl,
            packagingImageUrl = imagePackagingUrl,
            nutriscore = nutriscoreGrade,
            calories = nutriments?.energyKcal100g,
            proteins = nutriments?.proteins100g,
            carbohydrates = nutriments?.carbohydrates100g,
            fats = nutriments?.fat100g,
            salt = nutriments?.salt100g,
            sugars = nutriments?.sugars100g
        )
    }
}

data class Nutriments(
    @SerializedName("energy-kcal_100g") val energyKcal100g: Double?,
    @SerializedName("proteins_100g") val proteins100g: Double?,
    @SerializedName("carbohydrates_100g") val carbohydrates100g: Double?,
    @SerializedName("fat_100g") val fat100g: Double?,
    @SerializedName("salt_100g") val salt100g: Double?,
    @SerializedName("sugars_100g") val sugars100g: Double?
)
