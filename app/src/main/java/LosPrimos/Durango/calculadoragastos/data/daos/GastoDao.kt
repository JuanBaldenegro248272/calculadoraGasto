package LosPrimos.Durango.calculadoragastos.data.daos

import androidx.room.*
import LosPrimos.Durango.calculadoragastos.data.entities.Gasto
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface GastoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGasto(gasto: Gasto): Long
    @Update
    suspend fun updateGasto(gasto: Gasto): Int
    @Delete
    suspend fun deleteGasto(gasto: Gasto): Int
    @Query("SELECT * FROM gastos WHERE idUsuarioPaga = :idUsuario ORDER BY fecha DESC")
    fun getGastosByUsuario(idUsuario: Int): Flow<List<Gasto>>
    @Query("SELECT SUM(monto) FROM gastos WHERE idUsuarioPaga = :idUsuario AND fecha BETWEEN :inicio AND :fin")
    fun getSumaGastosPorPeriodo(idUsuario: Int, inicio: Long, fin: Long): Flow<Double?>
    @Query("SELECT * FROM gastos WHERE idUsuarioPaga = :idUsuario AND fecha BETWEEN :inicio AND :fin ORDER BY fecha DESC")
    fun getGastosPorPeriodo(idUsuario: Int, inicio: Long, fin: Long): Flow<List<Gasto>>
    @Query("SELECT * FROM gastos WHERE idGrupo = :idGrupo ORDER BY fecha DESC")
    fun getGastosByGrupo(idGrupo: Int): Flow<List<Gasto>>
}