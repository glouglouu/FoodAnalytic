package com.example.foodanalytic.api.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.foodanalytic.api.model.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    /**
     * Récupère tous les produits de la base de données.
     * Flow permet d'observer les changements en temps réel.
     */
    @Query("SELECT * FROM Product")
    fun getAll(): Flow<List<Product>>

    /**
     * Récupère un produit par son ID.
     */
    @Query("SELECT * FROM Product WHERE id = :id")
    suspend fun getById(id: Int): Product?

    /**
     * Insère un nouveau produit. Si un produit avec le même ID existe, il est remplacé.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: Product)

    /**
     * Met à jour les informations d'un produit existant.
     */
    @Update
    suspend fun update(product: Product)

    /**
     * Supprime un produit spécifique de la base de données.
     */
    @Delete
    suspend fun delete(product: Product)
}
