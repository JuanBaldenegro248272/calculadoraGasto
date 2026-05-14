package LosPrimos.Durango.calculadoragastos.viewModel

import LosPrimos.Durango.calculadoragastos.data.DataStoreManager
import LosPrimos.Durango.calculadoragastos.data.entities.Gasto
import LosPrimos.Durango.calculadoragastos.data.repositories.GastoRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GastoViewModel(private val repository: GastoRepository, private val dataStore: DataStoreManager) : ViewModel() {


    val usuarioActualId = dataStore.userIdFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )
    fun insertarGasto(gasto: Gasto) {
        viewModelScope.launch {
            repository.insertarGasto(gasto)
        }
    }

    fun actualizarGasto(gasto: Gasto) {
        viewModelScope.launch { repository.updateGasto(gasto) }
    }

    fun eliminarGasto(gasto: Gasto) {
        viewModelScope.launch { repository.deleteGasto(gasto) }
    }

    suspend fun obtenerGastoPorId(id: Int): Gasto? {
        return repository.obtenerGastoPorId(id)
    }

    fun obtenerGastosPorUsuario(usuarioId: Int?): Flow<List<Gasto>> {
        return repository.obtenerListagastosPorUsuario(usuarioId ?: 0)
    }

    fun obtenerGastosFijosPorUsuario(usuarioId: Int?): Flow<List<Gasto>> {
        return repository.obtenerListagastosFijosPorUsuario(usuarioId ?: 0)
    }
}
