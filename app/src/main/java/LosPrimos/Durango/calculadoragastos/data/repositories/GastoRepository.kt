package LosPrimos.Durango.calculadoragastos.data.repositories

import LosPrimos.Durango.calculadoragastos.data.daos.GastoDao
import LosPrimos.Durango.calculadoragastos.data.entities.Gasto
import kotlinx.coroutines.flow.Flow
import java.util.Date

class GastoRepository (private val gastoDao: GastoDao){

    suspend fun insertarGasto(gasto: Gasto): Int{
        return gastoDao.insertGasto(gasto)
    }

    suspend fun updateGasto(gasto: Gasto){
        gastoDao.updateGasto(gasto)
    }

    suspend fun deleteGasto(gasto: Gasto){
        gastoDao.deleteGasto(gasto)
    }

     fun gastosPorUsuario(idUsuario: Int): Flow<List<Gasto>> {
        return gastoDao.getGastosByUsuario(idUsuario)
    }

     fun gastosPorGrupo(idGrupo: Int): Flow<List<Gasto>>{
        return gastoDao.getGastosByGrupo(idGrupo)
    }

     fun obtenerSumaGastos(idUsuario: Int, inicio: Long, fin: Long): Flow<Double?> =
        gastoDao.getSumaGastosPorPeriodo(idUsuario, inicio, fin)

     fun obtenerListaGastos(idUsuario: Int, inicio: Long, fin: Long): Flow<List<Gasto>> =
        gastoDao.getGastosPorPeriodo(idUsuario, inicio, fin)
}