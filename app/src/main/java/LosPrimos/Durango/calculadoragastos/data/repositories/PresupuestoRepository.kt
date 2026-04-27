package LosPrimos.Durango.calculadoragastos.data.repositories

import LosPrimos.Durango.calculadoragastos.data.daos.PresupuestoDao
import LosPrimos.Durango.calculadoragastos.data.entities.Presupuesto

class PresupuestoRepository(private val presupuestoDao: PresupuestoDao){

    suspend fun insertarPresupuesto(presupuesto: Presupuesto): Int{
        return presupuestoDao.insertPresupuesto(presupuesto)
    }

    suspend fun deletePresupuesto(presupuesto: Presupuesto){
        presupuestoDao.deletePresupuesto(presupuesto)
    }

    suspend fun upatePresupuesto(presupuesto: Presupuesto){
        presupuestoDao.updatePresupuesto(presupuesto)
    }
}