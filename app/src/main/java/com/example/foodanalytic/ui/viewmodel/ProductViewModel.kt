package com.example.foodanalytic.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodanalytic.api.model.Product
import com.example.foodanalytic.api.repository.ProductRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Le ViewModel (Équivalent d'un Controller en NestJS).
 * Il reçoit les interactions de l'UI et gère l'état pour les écrans.
 */
class ProductViewModel(private val repository: ProductRepository) : ViewModel() {

    /**
     * Expose la liste des produits sous forme de StateFlow pour Compose.
     * Si la base de données change, l'UI est automatiquement mise à jour.
     */
    val products: StateFlow<List<Product>> = repository.allProducts
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /**
     * Ajoute un produit (appelé par l'interface).
     */
    fun addProduct(name: String, brand: String?, calories: Double?) {
        viewModelScope.launch {
            val product = Product(
                name = name,
                brand = brand,
                calories = calories,
                proteins = 0.0,
                carbohydrates = 0.0,
                fats = 0.0
            )
            repository.addProduct(product)
        }
    }

    /**
     * Supprime un produit (appelé par l'interface).
     */
    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            repository.deleteProduct(product)
        }
    }
}
