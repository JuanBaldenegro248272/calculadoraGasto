package LosPrimos.Durango.calculadoragastos.viewModel

import LosPrimos.Durango.calculadoragastos.data.DataStoreManager
import LosPrimos.Durango.calculadoragastos.data.entities.Ingreso
import LosPrimos.Durango.calculadoragastos.data.repositories.IngresoRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
}