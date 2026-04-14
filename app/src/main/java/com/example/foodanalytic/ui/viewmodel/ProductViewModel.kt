package com.example.foodanalytic.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodanalytic.api.model.Product
import com.example.foodanalytic.api.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductViewModel(private val repository: ProductRepository) : ViewModel() {

    val products: StateFlow<List<Product>> = repository.allProducts
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _scannedProduct = MutableStateFlow<Product?>(null)
    val scannedProduct: StateFlow<Product?> = _scannedProduct.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    /**
     * Recherche un produit par code-barres (via Repository -> API ou DB)
     */
    fun searchProductByBarcode(barcode: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val product = repository.getProductByBarcode(barcode)
            _scannedProduct.value = product
            _isLoading.value = false
        }
    }

    /**
     * Ajoute manuellement un produit.
     */
    fun addProduct(
        name: String,
        brand: String? = null,
        barcode: String? = null,
        calories: Double? = null,
        nutriscore: String? = null,
        imageUrl: String? = null
    ) {
        viewModelScope.launch {
            val product = Product(
                name = name,
                brand = brand,
                barcode = barcode,
                calories = calories,
                nutriscore = nutriscore,
                imageUrl = imageUrl,
                proteins = null,
                carbohydrates = null,
                fats = null,
                salt = null,
                sugars = null
            )
            repository.addProduct(product)
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            repository.deleteProduct(product)
        }
    }
}
