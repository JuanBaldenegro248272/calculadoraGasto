package LosPrimos.Durango.calculadoragastos.viewModel

import LosPrimos.Durango.calculadoragastos.data.DataStoreManager
import LosPrimos.Durango.calculadoragastos.data.entities.Grupo
import LosPrimos.Durango.calculadoragastos.data.repositories.GrupoRepository
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class GrupoViewModel(private val repository: GrupoRepository, dataStore: DataStoreManager): ViewModel() {

    var grupos by mutableStateOf<List<Grupo>>(emptyList())
        private set

    val usuarioActualId = dataStore.userIdFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )

    fun crearGrupo(grupo: Grupo){
        repository.crearGrupo(grupo)
    }

    fun obtenerGrupos(usuarioId: Int?){
        repository.obtenerGrupos(usuarioId){lista -> grupos = lista}
    }
}