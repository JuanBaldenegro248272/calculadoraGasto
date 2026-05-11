package LosPrimos.Durango.calculadoragastos.data.daos

import androidx.room.*
import LosPrimos.Durango.calculadoragastos.data.entities.Presupuesto
import kotlinx.coroutines.flow.Flow

@Dao
interface PresupuestoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPresupuesto(presupuesto: Presupuesto): Long
    @Update
    suspend fun updatePresupuesto(presupuesto: Presupuesto): Int
    @Delete
    suspend fun deletePresupuesto(presupuesto: Presupuesto): Int
    @Query("SELECT * FROM presupuestos WHERE idUsuario = :idUsuario")
    fun obtenerPresupuestosPorUsuario(idUsuario: Int): Flow<List<Presupuesto>>
}
