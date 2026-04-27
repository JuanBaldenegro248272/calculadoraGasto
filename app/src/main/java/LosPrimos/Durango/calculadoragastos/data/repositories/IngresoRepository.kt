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

    suspend fun obtenerIngresos(): Flow<List<Ingreso>> {
        return ingresoDao.getAllIngresos()
    }

    suspend fun obtenerIngresoPorPeriodo(inicio: Date, fin: Date): Flow<List<Ingreso?>>{
        return ingresoDao.getTotalIngresosPeriodo(inicio, fin)
    }
}