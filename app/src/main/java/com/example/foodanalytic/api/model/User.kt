package com.example.foodanalytic.api.model

import androidx.room.*

/**
 * ÉTAPE 1 : L'ENTITÉ (La Table SQL)
 * On définit ici la structure d'une ligne dans la base de données.
 */
@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val password: String // Ajouté pour le login/register
)
