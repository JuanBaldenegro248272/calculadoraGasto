package LosPrimos.Durango.calculadoragastos.viewModel

import LosPrimos.Durango.calculadoragastos.data.DataStoreManager
import LosPrimos.Durango.calculadoragastos.data.entities.Gasto
import LosPrimos.Durango.calculadoragastos.data.entities.Ingreso
import LosPrimos.Durango.calculadoragastos.data.repositories.IngresoRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class IngresoViewModel(private val repository: IngresoRepository, dataStore: DataStoreManager): ViewModel(){

    val usuarioActualId = dataStore.userIdFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )

    fun insertarIngreso(ingreso: Ingreso){
        viewModelScope.launch {
            repository.insertIngreso(ingreso)
        }
    }

    fun actualizarIngreso(ingreso: Ingreso) {
        viewModelScope.launch { repository.updateIngreso(ingreso) }
    }

    fun eliminarIngreso(ingreso: Ingreso) {
        viewModelScope.launch { repository.deleteIngreso(ingreso) }
    }

    suspend fun obtenerIngresoPorId(id: Int): Ingreso? {
        return repository.obtenerIngresoPorId(id)
    }

    fun obtenerIngresosPorUsuario(idUsuario: String?): Flow<List<Ingreso>> {
        return repository.obtenerIngresosPorUsuario(idUsuario ?: "")
    }

    fun obtenerIngresosFijosPorUsuario(usuarioId: String?): Flow<List<Ingreso>> {
        return repository.obtenerIngresosFijosPorUsuario(usuarioId ?: "")
    }
}