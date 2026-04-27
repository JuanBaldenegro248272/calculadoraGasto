package LosPrimos.Durango.calculadoragastos.data.daos

import androidx.room.*
import LosPrimos.Durango.calculadoragastos.data.entities.Presupuesto
import kotlinx.coroutines.flow.Flow

@Dao
interface PresupuestoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPresupuesto(presupuesto: Presupuesto): Int

    @Update
    suspend fun updatePresupuesto(presupuesto: Presupuesto)

    @Delete
    suspend fun deletePresupuesto(presupuesto: Presupuesto)

}
