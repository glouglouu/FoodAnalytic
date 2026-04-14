package com.example.foodanalytic.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(productDao: ProductDao) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    // Configuration du scanner ML Kit
    val options = GmsBarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
        .build()
    val scanner = GmsBarcodeScanning.getClient(context, options)

    // Observation des produits stockés localement
    val products by productDao.getAll().collectAsState(initial = emptyList())

    var queryText by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }

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
                                        // Open Food Facts renvoie status = 1 si le produit est trouvé
                                        if (body?.status == 1 && body.product != null) {
                                            // Conversion propre du modèle API vers notre modèle local
                                            val newProduct = body.product.toProduct(code)
                                            
                                            productDao.insert(newProduct)
                                            Toast.makeText(context, "Produit ajouté : ${newProduct.name}", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(context, "Produit non trouvé sur Open Food Facts", Toast.LENGTH_SHORT).show()
                                        }
                                    } else {
                                        Toast.makeText(context, "Erreur API : ${response.code()}", Toast.LENGTH_SHORT).show()
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Erreur réseau : ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(context, "Erreur scanner : ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                },
                icon = { Icon(Icons.Default.QrCodeScanner, contentDescription = null) },
                text = { Text("Scanner") },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
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
                            if (isSearchActive) {
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
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(3) { index ->
                        Text("Suggestion ${index + 1}", modifier = Modifier.padding(16.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Derniers produits scannés",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (products.isEmpty()) {
                Text(
                    text = "Aucun produit scanné pour le moment.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 80.dp) // Pour éviter que le FAB cache le dernier item
                ) {
                    items(products) { product ->
                        ProductItemCard(product = product)
                    }
                }
            }
        }
    }
}
