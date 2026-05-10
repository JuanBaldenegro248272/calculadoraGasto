package LosPrimos.Durango.calculadoragastos.viewModel

import LosPrimos.Durango.calculadoragastos.data.DataStoreManager
import LosPrimos.Durango.calculadoragastos.data.entities.Gasto
import LosPrimos.Durango.calculadoragastos.data.repositories.GastoRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
}
