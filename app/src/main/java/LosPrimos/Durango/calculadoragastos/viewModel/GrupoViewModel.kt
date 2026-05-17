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

    var grupoSeleccionado by mutableStateOf<Grupo?>(null)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    val usuarioActualId = dataStore.userIdFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )

    fun crearGrupo(grupo: Grupo){
        repository.crearGrupo(
            grupo = grupo,
            onSuccess = {
                obtenerGrupos(usuarioActualId.value)
            },
            onError = { exception ->
                errorMessage = exception.message
            }
        )
    }

    fun obtenerGrupos(usuarioId: String?){
        if (usuarioId.isNullOrBlank()) return

        repository.obtenerGrupos(
            usuarioId = usuarioId,
            onResult = { lista ->
                grupos = lista
            },
            onError = { exception ->
                errorMessage = exception.message
            }
        )
    }

    fun obtenerGrupoPorId(grupoId: String) {
        if (grupoId.isBlank()) return

        repository.obtenerGrupoPorId(
            grupoId = grupoId,
            onResult = { grupo ->
                grupoSeleccionado = grupo
            },
            onError = { exception ->
                errorMessage = exception.message
            }
        )
    }

    fun unirseAGrupo(codigo: String, usuarioId: String) {
        if (codigo.isBlank() || usuarioId.isBlank()) return

        repository.unirseAGrupoPorCodigo(
            codigo = codigo,
            usuarioId = usuarioId,
            onSuccess = {
                obtenerGrupos(usuarioId)
            },
            onError = { exception ->
                errorMessage = exception.message
            }
        )
    }





}
