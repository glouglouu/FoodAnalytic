package com.example.foodanalytic.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.example.foodanalytic.api.model.Product

@Composable
fun ProductItemCard(product: Product, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val displayImageUrl = product.frontImageUrl ?: product.imageUrl

            Surface(
                modifier = Modifier.size(60.dp),
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.secondaryContainer
            ) {
                if (displayImageUrl != null) {
                    // Utilisation de SubcomposeAsyncImage pour avoir des états (loading/error)
                    SubcomposeAsyncImage(
                        model = displayImageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        loading = {
                            CircularProgressIndicator(
                                modifier = Modifier.padding(16.dp),
                                strokeWidth = 2.dp
                            )
                        },
                        error = {
                            Icon(Icons.Default.BrokenImage, contentDescription = null, modifier = Modifier.padding(16.dp))
                        }
                    )
                } else {
                    Icon(Icons.Default.Fastfood, contentDescription = null, modifier = Modifier.padding(16.dp))
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = product.brand ?: "Marque inconnue",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
