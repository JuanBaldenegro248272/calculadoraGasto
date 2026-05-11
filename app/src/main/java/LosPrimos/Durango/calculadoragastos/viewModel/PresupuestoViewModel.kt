package LosPrimos.Durango.calculadoragastos.viewModel

import LosPrimos.Durango.calculadoragastos.data.DataStoreManager
import LosPrimos.Durango.calculadoragastos.data.entities.Presupuesto
import LosPrimos.Durango.calculadoragastos.data.repositories.PresupuestoRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PresupuestoViewModel(private val repository : PresupuestoRepository, dataStore: DataStoreManager): ViewModel(){

    val usuarioActualId = dataStore.userIdFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )

    fun insertarPresupuesto(presupuesto: Presupuesto){
        viewModelScope.launch {
            repository.insertarPresupuesto(presupuesto)
        }
    }
    
    fun obtenerPresupuesto(usuarioId: Int): Flow<List<Presupuesto>> {
        return repository.obtenerPresupuestos(usuarioId)
    }
}