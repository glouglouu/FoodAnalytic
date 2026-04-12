package com.example.foodanalytic.api.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * ÉTAPE 1 : L'ENTITÉ (La Table SQL)
 * On définit ici la structure d'une ligne dans la base de données.
 * @Entity : Transforme cette classe en table SQL nommée "User".
 */
@Entity
data class User(
    // @PrimaryKey : Identifiant unique pour chaque utilisateur
    // autoGenerate = true : Room crée l'ID automatiquement (1, 2, 3...).
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String
)