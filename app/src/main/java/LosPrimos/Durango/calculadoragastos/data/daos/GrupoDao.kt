package LosPrimos.Durango.calculadoragastos.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import LosPrimos.Durango.calculadoragastos.data.entities.Grupo
import kotlinx.coroutines.flow.Flow

@Dao
interface GrupoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGrupo(grupo: Grupo): Int

    @Update
    suspend fun updateGrupo(grupo: Grupo)

    @Delete
    suspend fun deleteGrupo(grupo: Grupo)

    @Query("SELECT * FROM grupos")
    fun getAllGrupos(): Flow<List<Grupo>>

    @Query("SELECT * FROM grupos WHERE idGrupo = :idGrupo")
    suspend fun getGrupoById(idGrupo: Int): Grupo?
}
