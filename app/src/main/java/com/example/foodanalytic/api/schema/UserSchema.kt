package com.example.foodanalytic.api.schema

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * ÉTAPE 1 : L'ENTITÉ (La Table SQL)
 * On définit ici la structure d'une ligne dans la base de données.
 * @Entity : Transforme cette classe en table SQL nommée "Task".
 */
@Entity
data class User(
    // @PrimaryKey : Identifiant unique pour chaque tâche.
    // autoGenerate = true : Room crée l'ID automatiquement (1, 2, 3...).
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String
)
/**
 * ÉTAPE 2 : LE DAO (Data Access Object)
 * C'est l'interface qui contient toutes les actions (requêtes) possibles.
 */
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
     * @Insert : Ajoute une nouvelle tâche.
     * suspend : Indique que cette fonction est asynchrone (Coroutine).
     * Elle doit s'exécuter en arrière-plan pour ne pas bloquer l'écran.
     */
    @Insert
    suspend fun insert(user: User)
    /**
     * @Delete : Supprime une tâche précise de la base.
     * suspend : Également exécuté en arrière-plan.
     */
    @Delete
    suspend fun delete(user: User)
}
/**
 * ÉTAPE 3 : LA DATABASE (Le point d'accès)
 * C'est le centre de contrôle qui fait le lien entre les entités et le DAO.
 */
@Database(entities = [User::class], version = 1)
abstract class AppDb : RoomDatabase() {
    // Cette fonction permet de récupérer les commandes du DAO.
    abstract fun dao(): UserDao
}