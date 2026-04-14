package com.example.foodanalytic.api.repository

import com.example.foodanalytic.api.data.ProductDao
import com.example.foodanalytic.api.model.Product
import com.example.foodanalytic.api.network.RetrofitClient
import kotlinx.coroutines.flow.Flow

/**
 * Le Repository gère la logique de données (API + DB locale).
 */
class ProductRepository(private val productDao: ProductDao) {

    val allProducts: Flow<List<Product>> = productDao.getAll()

    /**
     * Recherche un produit par code-barres. 
     * Tente d'abord en local, puis sur l'API Open Food Facts si non trouvé.
     */
    suspend fun getProductByBarcode(barcode: String): Product? {
        // 1. Vérifier en base locale
        val localProduct = productDao.getByBarcode(barcode)
        if (localProduct != null) return localProduct

        // 2. Si non trouvé, appeler l'API
        try {
            val response = RetrofitClient.instance.getProductInfo(barcode)
            if (response.isSuccessful && response.body()?.status == 1) {
                val apiProduct = response.body()?.product
                if (apiProduct != null) {
                    val newProduct = apiProduct.toProduct(barcode)
                    // Optionnel : Sauvegarder automatiquement en base
                    productDao.insert(newProduct)
                    return newProduct
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    suspend fun addProduct(product: Product) {
        productDao.insert(product)
    }

    suspend fun deleteProduct(product: Product) {
        productDao.delete(product)
    }

    suspend fun getProductById(id: Int): Product? {
        return productDao.getById(id)
    }
}
