package LosPrimos.Durango.calculadoragastos.data.repositories

import LosPrimos.Durango.calculadoragastos.data.daos.GrupoDao
import LosPrimos.Durango.calculadoragastos.data.entities.Grupo
import kotlinx.coroutines.flow.Flow

class GrupoRepository(private val grupoDao: GrupoDao){

    suspend fun insertGrupo(grupo: Grupo): Int{
        return grupoDao.insertGrupo(grupo)
    }

    suspend fun deleteGrupo(grupo: Grupo){
        grupoDao.deleteGrupo(grupo)
    }

    suspend fun update(grupo: Grupo){
        grupoDao.updateGrupo(grupo)
    }

    suspend fun obtenerGrupoID(idGrupo: Int): Grupo?{
        return grupoDao.getGrupoById(idGrupo)
    }

    suspend fun obtenerGrupo(): Flow<List<Grupo?>>{
        return grupoDao.getAllGrupos()
    }
}