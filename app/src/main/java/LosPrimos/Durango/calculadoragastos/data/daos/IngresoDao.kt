package LosPrimos.Durango.calculadoragastos.data.daos

import LosPrimos.Durango.calculadoragastos.data.entities.Ingreso
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface IngresoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngreso(ingreso: Ingreso): Long
    @Update
    suspend fun updateIngreso(ingreso: Ingreso): Int
    @Delete
    suspend fun deleteIngreso(ingreso: Ingreso): Int

    @Query("SELECT * FROM ingresos WHERE idIngreso = :id")
    suspend fun getIngresoById(id: Int): Ingreso?
    @Query("SELECT * FROM ingresos WHERE idUsuario = :idUsuario ORDER BY fecha DESC")
    fun getIngresosByUsuario(idUsuario: Int): Flow<List<Ingreso>>
    @Query("SELECT SUM(monto) FROM ingresos WHERE idUsuario = :idUsuario AND fecha BETWEEN :inicio AND :fin")
    fun getSumaIngresosPorPeriodo(idUsuario: Int, inicio: Long, fin: Long): Flow<Double?>
    @Query("SELECT * FROM ingresos WHERE idUsuario = :idUsuario AND fecha BETWEEN :inicio AND :fin ORDER BY fecha DESC")
    fun getIngresosPorPeriodo(idUsuario: Int, inicio: Long, fin: Long): Flow<List<Ingreso>>

    @Query("SELECT * FROM ingresos WHERE idUsuario = :idUsuario AND esFijo = 1 ORDER BY fecha DESC")
    fun getIngresosFijosByUsuario(idUsuario: Int): Flow<List<Ingreso>>
}
