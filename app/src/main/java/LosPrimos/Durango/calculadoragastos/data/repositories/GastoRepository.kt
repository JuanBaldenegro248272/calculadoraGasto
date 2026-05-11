package LosPrimos.Durango.calculadoragastos.data.repositories

import LosPrimos.Durango.calculadoragastos.data.daos.GastoDao
import LosPrimos.Durango.calculadoragastos.data.entities.Gasto
import kotlinx.coroutines.flow.Flow
import java.util.Date

class GastoRepository (private val gastoDao: GastoDao){

    suspend fun insertarGasto(gasto: Gasto): Long{
        return gastoDao.insertGasto(gasto)
    }

    suspend fun updateGasto(gasto: Gasto): Int{
        return gastoDao.updateGasto(gasto)
    }

    suspend fun deleteGasto(gasto: Gasto): Int{
        return gastoDao.deleteGasto(gasto)
    }

    suspend fun obtenerGastoPorId(id: Int): Gasto? = gastoDao.getGastoById(id)

     fun obtenerListagastosPorUsuario(idUsuario: Int): Flow<List<Gasto>> {
        return gastoDao.getGastosByUsuario(idUsuario)
    }

     fun obtenergastosPorGrupo(idGrupo: Int): Flow<List<Gasto>>{
        return gastoDao.getGastosByGrupo(idGrupo)
    }

     fun obtenerSumaGastosPorPeriodo(idUsuario: Int, inicio: Long, fin: Long): Flow<Double?> =
        gastoDao.getSumaGastosPorPeriodo(idUsuario, inicio, fin)

     fun obtenerListaGastosDeUsuarioPorPeriodo(idUsuario: Int, inicio: Long, fin: Long): Flow<List<Gasto>> =
        gastoDao.getGastosPorPeriodo(idUsuario, inicio, fin)
}