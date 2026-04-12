package com.example.foodanalytic.api.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.foodanalytic.api.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    /**
     * @Query : On écrit du SQL pur. Ici, on récupère tout.
     * Flow : Permet d'observer la base en temps réel. Si une donnée change,
     * l'interface est notifiée automatiquement sans relancer la requête.
     */
    @Query("SELECT * FROM User")
    fun getAll(): Flow<List<User>>
    /**
     * @Insert : Ajoute un nouvel utilisateur.
     * suspend : Indique que cette fonction est asynchrone (Coroutine).
     * Elle doit s'exécuter en arrière-plan pour ne pas bloquer l'écran.
     */
    @Insert
    suspend fun insert(user: User)
    /**
     * @Delete : Supprime un utilisateur précis de la base.
     * suspend : Également exécuté en arrière-plan.
     */
    @Delete
    suspend fun delete(user: User)
}