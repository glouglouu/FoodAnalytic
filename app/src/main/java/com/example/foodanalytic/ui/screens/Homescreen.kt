package com.example.foodanalytic.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.foodanalytic.api.data.ProductDao
import com.example.foodanalytic.api.network.RetrofitClient
import com.example.foodanalytic.ui.components.ProductItemCard
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(productDao: ProductDao, onProductClick: (Int) -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val options = GmsBarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
        .build()

    val scanner = GmsBarcodeScanning.getClient(context, options)
    val products by productDao.getAll().collectAsState(initial = emptyList())

    var queryText by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }

    // Filtrage dynamique de la liste des produits
    val filteredProducts = remember(products, queryText) {
        if (queryText.isBlank()) {
            products
        } else {
            products.filter { product ->
                product.name.contains(queryText, ignoreCase = true) ||
                        (product.brand?.contains(queryText, ignoreCase = true) == true) ||
                        (product.barcode?.contains(queryText) == true)
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    scanner.startScan()
                        .addOnSuccessListener { barcode ->
                            val code = barcode.rawValue ?: return@addOnSuccessListener
                            scope.launch {
                                try {
                                    val response = RetrofitClient.instance.getProductInfo(code)
                                    if (response.isSuccessful) {
                                        val body = response.body()
                                        if (body?.status == 1 && body.product != null) {
                                            val newProduct = body.product.toProduct(code)
                                            
                                            withContext(Dispatchers.IO) {
                                                productDao.insert(newProduct)
                                            }
                                            
                                            Toast.makeText(context, "Produit ajouté : ${newProduct.name}", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(context, "Produit non trouvé", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Erreur scan : ${e.message}", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(context, "Erreur scanner : ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                },
                icon = { Icon(Icons.Default.QrCodeScanner, contentDescription = null) },
                text = { Text("Scanner") }
            )
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            SearchBar(
                modifier = Modifier.fillMaxWidth(),
                inputField = {
                    SearchBarDefaults.InputField(
                        query = queryText,
                        onQueryChange = { queryText = it },
                        onSearch = { isSearchActive = false },
                        expanded = isSearchActive,
                        onExpandedChange = { isSearchActive = it },
                        placeholder = { Text("Rechercher un produit...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        trailingIcon = {
                            if (queryText.isNotEmpty()) {
                                IconButton(onClick = { queryText = "" }) {
                                    Icon(Icons.Default.Close, contentDescription = "Effacer")
                                }
                            }
                        }
                    )
                },
                expanded = isSearchActive,
                onExpandedChange = { isSearchActive = it },
            ) {
                // Liste affichée dans la barre de recherche lorsqu'elle est active (étendue)
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredProducts) { product ->
                        ProductItemCard(product = product, onClick = {
                            onProductClick(product.id)
                            isSearchActive = false
                        })
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = if (queryText.isEmpty()) "Historique des scans" else "Résultats de recherche",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (filteredProducts.isEmpty()) {
                Text(
                    text = if (queryText.isEmpty()) "Aucun produit scanné pour le moment." else "Aucun produit trouvé pour \"$queryText\"",
                    modifier = Modifier.padding(top = 8.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(filteredProducts) { product ->
                        ProductItemCard(product = product, onClick = { onProductClick(product.id) })
                    }
                }
            }
        }
    }
}
