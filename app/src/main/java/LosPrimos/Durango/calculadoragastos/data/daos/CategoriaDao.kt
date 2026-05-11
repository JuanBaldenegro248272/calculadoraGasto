package LosPrimos.Durango.calculadoragastos.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import LosPrimos.Durango.calculadoragastos.data.entities.Categoria
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriaDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategoria(categoria: Categoria): Long

    @Update
    suspend fun updateCategoria(categoria: Categoria): Int

    @Delete
    suspend fun deleteCategoria(categoria: Categoria): Int

    @Query("SELECT * FROM categorias")
    fun getAllCategorias(): Flow<List<Categoria>>

    @Query("SELECT * FROM categorias WHERE idCategoria = :idCategoria LIMIT 1")
    suspend fun getCategoriaById(idCategoria: Int): List<Categoria>
}
