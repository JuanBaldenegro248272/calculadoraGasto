package LosPrimos.Durango.calculadoragastos.data.repositories

import LosPrimos.Durango.calculadoragastos.data.daos.TarjetaDao
import LosPrimos.Durango.calculadoragastos.data.entities.Tarjeta
import kotlinx.coroutines.flow.Flow

class TarjetaRepository(private val tarjetaDao: TarjetaDao){

    suspend fun insertTarjeta(tarjeta: Tarjeta): Int{
        return insertTarjeta(tarjeta)
    }

    suspend fun updateTarjeta(tarjeta: Tarjeta){
        updateTarjeta(tarjeta)
    }

    suspend fun getAllTarjetas(): Flow<List<Tarjeta?>>{
        return tarjetaDao.getAllTarjetas()
    }

    suspend fun getTarjetaID(idTarjeta: Int): Tarjeta?{
        return tarjetaDao.getTarjetaById(idTarjeta)
    }
}