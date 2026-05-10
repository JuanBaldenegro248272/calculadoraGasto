package LosPrimos.Durango.calculadoragastos.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import LosPrimos.Durango.calculadoragastos.data.entities.Usuario
import LosPrimos.Durango.calculadoragastos.data.repositories.UsuarioRepository
import LosPrimos.Durango.calculadoragastos.data.DataStoreManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PerfilViewModel(
    private val repository: UsuarioRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val userIdFlow = dataStoreManager.userIdFlow

    val usuarioState: StateFlow<Usuario?> = userIdFlow
        .flatMapLatest { id ->
            if (id != null) repository.obtenerUsuarioPorId(id) else flowOf(null)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun actualizarPerfil(nombre: String, genero: String, fechaNac: Long) {
        val usuarioActual = usuarioState.value ?: return

        viewModelScope.launch {
            val usuarioEditado = usuarioActual.copy(
                nombre = nombre,
                genero = genero,
                fechaNacimiento = fechaNac
            )
            repository.updateUsuario(usuarioEditado)
        }
    }
    fun actualizarFotoPerfil(fotoUri: String) {
        val usuarioActual = usuarioState.value ?: return

        viewModelScope.launch {
            val usuarioEditado = usuarioActual.copy(
                fotoPerfil = fotoUri
            )
            repository.updateUsuario(usuarioEditado)
        }
    }


}