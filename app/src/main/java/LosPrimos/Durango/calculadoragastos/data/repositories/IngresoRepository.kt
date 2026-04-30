package LosPrimos.Durango.calculadoragastos.data.repositories

import LosPrimos.Durango.calculadoragastos.data.daos.IngresoDao
import LosPrimos.Durango.calculadoragastos.data.entities.Ingreso
import kotlinx.coroutines.flow.Flow
import java.util.Date

class IngresoRepository(private val ingresoDao: IngresoDao){

    suspend fun insertIngreso(ingreso: Ingreso): Int{
        return ingresoDao.insertIngreso(ingreso)
    }

    suspend fun updateIngreso(ingreso: Ingreso){
        ingresoDao.updateIngreso(ingreso)
    }

    suspend fun deleteIngreso(ingreso: Ingreso){
        ingresoDao.deleteIngreso(ingreso)
    }

     fun obtenerIngresos(): Flow<List<Ingreso>> {
        return ingresoDao.getAllIngresos()
    }

     fun obtenerSumaIngresos(idUsuario: Int, inicio: Long, fin: Long): Flow<Double?> =
        ingresoDao.getSumaIngresosPorPeriodo(idUsuario, inicio, fin)

     fun obtenerListaIngresos(idUsuario: Int, inicio: Long, fin: Long): Flow<List<Ingreso>> =
        ingresoDao.getIngresosPorPeriodo(idUsuario, inicio, fin)
}