package com.example.foodanalytic.api.repository

import com.example.foodanalytic.api.data.ProductDao
import com.example.foodanalytic.api.model.Product
import kotlinx.coroutines.flow.Flow

/**
 * Le Repository (Équivalent d'un Service en NestJS).
 * Il gère la logique de données et communique avec le DAO.
 */
class ProductRepository(private val productDao: ProductDao) {

    /**
     * Récupère la liste de tous les produits (en temps réel).
     */
    val allProducts: Flow<List<Product>> = productDao.getAll()

    /**
     * Ajoute un produit dans la base.
     */
    suspend fun addProduct(product: Product) {
        productDao.insert(product)
    }

    /**
     * Supprime un produit de la base.
     */
    suspend fun deleteProduct(product: Product) {
        productDao.delete(product)
    }

    /**
     * Trouve un produit par son ID.
     */
    suspend fun getProductById(id: Int): Product? {
        return productDao.getById(id)
    }
}
