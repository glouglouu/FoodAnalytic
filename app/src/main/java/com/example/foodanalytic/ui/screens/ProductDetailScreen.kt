package com.example.foodanalytic.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.foodanalytic.api.data.ProductDao
import com.example.foodanalytic.api.model.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: Int,
    productDao: ProductDao,
    onBackClick: () -> Unit
) {
    // Dans un vrai projet, on utiliserait un ViewModel, mais on simplifie ici
    val products by productDao.getAll().collectAsState(initial = emptyList())
    val product = products.find { it.id == productId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(product?.name ?: "Détails") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (product == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Carrousel d'images
                ImageCarousel(product)

                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = product.brand ?: "Marque inconnue",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Nutriscore et Calories
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        InfoCard(
                            label = "Nutriscore",
                            value = product.nutriscore?.uppercase() ?: "?",
                            containerColor = getNutriscoreColor(product.nutriscore)
                        )
                        InfoCard(
                            label = "Calories",
                            value = "${product.calories?.toInt() ?: 0} kcal",
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Nutriments
                    Text(
                        text = "Valeurs nutritionnelles (pour 100g)",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    NutrientRow("Protéines", "${product.proteins ?: 0.0} g")
                    NutrientRow("Glucides", "${product.carbohydrates ?: 0.0} g")
                    NutrientRow("Dont sucres", "${product.sugars ?: 0.0} g")
                    NutrientRow("Matières grasses", "${product.fats ?: 0.0} g")
                    NutrientRow("Sel", "${product.salt ?: 0.0} g")
                }
            }
        }
    }
}

@Composable
fun ImageCarousel(product: Product) {
    val images = listOfNotNull(
        product.imageUrl,
        product.frontImageUrl,
        product.ingredientsImageUrl,
        product.nutritionImageUrl,
        product.packagingImageUrl
    ).distinct()

    if (images.isNotEmpty()) {
        val pagerState = rememberPagerState(pageCount = { images.size })

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                AsyncImage(
                    model = images[page],
                    contentDescription = "Image du produit",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }

            // Indicateurs de page (dots)
            if (images.size > 1) {
                Row(
                    Modifier
                        .height(50.dp)
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(images.size) { iteration ->
                        val color = if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                        Box(
                            modifier = Modifier
                                .padding(2.dp)
                                .clip(CircleShape)
                                .background(color)
                                .size(8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InfoCard(label: String, value: String, containerColor: Color) {
    Card(
        colors = CardDefaults.cardColors(containerColor = containerColor),
        modifier = Modifier.width(150.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, style = MaterialTheme.typography.labelMedium)
            Text(text = value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun NutrientRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label)
        Text(text = value, fontWeight = FontWeight.Bold)
    }
}

fun getNutriscoreColor(score: String?): Color {
    return when (score?.lowercase()) {
        "a" -> Color(0xFF008b4c)
        "b" -> Color(0xFF85bb2f)
        "c" -> Color(0xFFfecb02)
        "d" -> Color(0xFFee8100)
        "e" -> Color(0xFFe63e11)
        else -> Color.Gray
    }
}
