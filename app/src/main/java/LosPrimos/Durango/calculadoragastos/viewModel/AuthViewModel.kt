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

    val userId = dataStore.userIdFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
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

    fun register(usuario: Usuario) {
        if (usuario.correo.isBlank() || usuario.hashContrasena.isBlank() || usuario.nombre.isBlank()) {
            _authState.value = AuthState.Error("Por favor llena todos los campos obligatorios")
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading

            try {
                val usuarioExistente = usuarioRepository.verificarCorreoExistente(usuario.correo)

                if (usuarioExistente != null) {
                    _authState.value = AuthState.Error("Este correo electrónico ya está en uso")
                    return@launch // Detenemos la ejecución aquí
                }
                val nuevoId = usuarioRepository.insertarUsuario(usuario)

                if (nuevoId > 0) {
                    dataStore.saveSession(nuevoId.toInt())
                    _authState.value = AuthState.Success(nuevoId.toInt())
                } else {
                    _authState.value = AuthState.Error("Ocurrió un error al crear la cuenta")
                }

            } catch (e: Exception) {
                _authState.value = AuthState.Error("Error: ${e.message}")
            }
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }


    private val _usuarioRecordado = MutableStateFlow<Usuario?>(null)
    val usuarioRecordado: StateFlow<Usuario?> = _usuarioRecordado.asStateFlow()

    init {
        viewModelScope.launch {
            dataStore.userIdFlow.collect { id ->
                if (id != null && id != 0) {
                    val usuario = usuarioRepository.obtenerUusarioPorId(id)
                    _usuarioRecordado.value = usuario
                } else {
                    _usuarioRecordado.value = null
                }
            }
        }
    }

    fun desvincularCuenta() {
        viewModelScope.launch {
            dataStore.clearRememberedUser()
            _usuarioRecordado.value = null
        }
    }
}
