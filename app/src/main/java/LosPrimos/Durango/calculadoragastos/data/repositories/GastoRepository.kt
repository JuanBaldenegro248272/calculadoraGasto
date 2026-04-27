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

    suspend fun gastosPorUsuario(idUsuario: Int): Flow<List<Gasto>> {
        return gastoDao.getGastosByUsuario(idUsuario)
    }

    suspend fun gastosPorGrupo(idGrupo: Int): Flow<List<Gasto>>{
        return gastoDao.getGastosByGrupo(idGrupo)
    }

    suspend fun gastosPorPeriodo(idUsuario: Int, inicio: Date, fin: Date): Flow<List<Gasto>>{
        return gastoDao.getTotalGastosUsuarioPeriodo(idUsuario, inicio, fin)
    }
}