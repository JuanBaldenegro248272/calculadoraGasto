package LosPrimos.Durango.calculadoragastos.data.daos

import LosPrimos.Durango.calculadoragastos.data.entities.Tarjeta
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TarjetaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTarjeta(tarjeta: Tarjeta): Long

    @Update
    suspend fun updateTarjeta(tarjeta: Tarjeta): Int

    @Delete
    suspend fun deleteTarjeta(tarjeta: Tarjeta): Int

    @Query("SELECT * FROM tarjetas WHERE idUsuario = :idUsuario")
    fun getTarjetasByUsuario(idUsuario: String): Flow<List<Tarjeta>>

    @Query("SELECT * FROM tarjetas WHERE idTarjeta = :idTarjeta LIMIT 1")
    suspend fun getTarjetaById(idTarjeta: Int): List<Tarjeta>
}