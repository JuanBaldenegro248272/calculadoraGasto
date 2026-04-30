package LosPrimos.Durango.calculadoragastos.data.daos

import LosPrimos.Durango.calculadoragastos.data.entities.Ingreso
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface IngresoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngreso(ingreso: Ingreso): Int

    @Update
    suspend fun updateIngreso(ingreso: Ingreso)

    @Delete
    suspend fun deleteIngreso(ingreso: Ingreso)

    @Query("SELECT * FROM ingresos ORDER BY fecha DESC")
    fun getAllIngresos(): Flow<List<Ingreso>>

    @Query("SELECT SUM(monto) FROM ingresos WHERE idUsuario = :idUsuario AND fecha BETWEEN :inicio AND :fin")
    fun getSumaIngresosPorPeriodo(idUsuario: Int, inicio: Long, fin: Long): Flow<Double?>

    @Query("SELECT * FROM ingresos WHERE idUsuario = :idUsuario AND fecha BETWEEN :inicio AND :fin ORDER BY fecha DESC")
    fun getIngresosPorPeriodo(idUsuario: Int, inicio: Long, fin: Long): Flow<List<Ingreso>>
}
