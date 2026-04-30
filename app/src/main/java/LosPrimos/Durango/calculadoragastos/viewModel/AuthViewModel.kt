package LosPrimos.Durango.calculadoragastos.viewModel

import LosPrimos.Durango.calculadoragastos.data.DataStoreManager
import LosPrimos.Durango.calculadoragastos.data.entities.Usuario
import LosPrimos.Durango.calculadoragastos.data.repositories.UsuarioRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val userId: Int) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(private val usuarioRepository: UsuarioRepository, private val dataStore: DataStoreManager): ViewModel() {

    val isLoggedIn = dataStore.isLoggedInFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        false
    )

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun login(correo: String, contrasena: String) {
        if (correo.isBlank() || contrasena.isBlank()) {
            _authState.value = AuthState.Error("Por favor, llena todos los campos")
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading

            try {
                val usuario = usuarioRepository.loginUsuario(correo, contrasena)

                if (usuario != null) {
                    dataStore.saveSession(usuario.idUsuario)
                    _authState.value = AuthState.Success(usuario.idUsuario)
                } else {
                    _authState.value = AuthState.Error("Correo o contraseña incorrectos")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Error: ${e.message}")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            dataStore.logout()
            _authState.value = AuthState.Idle
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }


    private val _usuarioRecordado = MutableStateFlow<Usuario?>(null)
    val usuarioRecordado: StateFlow<Usuario?> = _usuarioRecordado.asStateFlow()

    fun desvincularCuenta() {
        // Función para el botón "No Eres Hector?"
        _usuarioRecordado.value = null
    }
}