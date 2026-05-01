package LosPrimos.Durango.calculadoragastos.data.repositories

import LosPrimos.Durango.calculadoragastos.data.daos.TarjetaDao
import LosPrimos.Durango.calculadoragastos.data.entities.Tarjeta
import kotlinx.coroutines.flow.Flow

class TarjetaRepository(private val tarjetaDao: TarjetaDao){

    suspend fun insertTarjeta(tarjeta: Tarjeta): Long{
        return tarjetaDao.insertTarjeta(tarjeta)
    }

    suspend fun updateTarjeta(tarjeta: Tarjeta){
        tarjetaDao.updateTarjeta(tarjeta)
    }

    suspend fun deleteTarjeta(tarjeta: Tarjeta){
        tarjetaDao.deleteTarjeta(tarjeta)
    }

     fun getAllTarjetasPorUsuario(idUsuario: Int): Flow<List<Tarjeta>>{
        return tarjetaDao.getTarjetasByUsuario(idUsuario)
    }

    suspend fun getTarjetaID(idTarjeta: Int): Tarjeta?{
        return tarjetaDao.getTarjetaById(idTarjeta).firstOrNull()
    }
}