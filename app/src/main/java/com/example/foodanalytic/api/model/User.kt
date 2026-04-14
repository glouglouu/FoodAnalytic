package com.example.foodanalytic.api.model

import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize

/**
 * Entité User rendue Parcelable pour être utilisée en toute sécurité 
 * avec rememberSaveable dans Compose.
 */
@Parcelize
@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val password: String
) : Parcelable
