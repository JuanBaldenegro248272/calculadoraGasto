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
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategoria(categoria: Categoria): Long

    @Update
    suspend fun updateCategoria(categoria: Categoria)

    @Delete
    suspend fun deleteCategoria(categoria: Categoria)

    @Query("SELECT * FROM categorias")
    fun getAllCategorias(): Flow<List<Categoria>>

    @Query("SELECT * FROM categorias WHERE idCategoria = :idCategoria")
    suspend fun getCategoriaById(idCategoria: Int): Categoria?
}
