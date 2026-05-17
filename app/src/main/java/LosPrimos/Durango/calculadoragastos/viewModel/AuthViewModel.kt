package LosPrimos.Durango.calculadoragastos.viewModel

import LosPrimos.Durango.calculadoragastos.data.DataStoreManager
import LosPrimos.Durango.calculadoragastos.data.entities.Usuario
import LosPrimos.Durango.calculadoragastos.data.repositories.UsuarioRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val userId: String) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(private val usuarioRepository: UsuarioRepository, private val dataStore: DataStoreManager): ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _sesionVerificada = MutableStateFlow(false)
    val sesionVerificada: StateFlow<Boolean> = _sesionVerificada.asStateFlow()

    val isLoggedIn = dataStore.isLoggedInFlow.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
    val userId = dataStore.userIdFlow.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun verificarSesion() {
        viewModelScope.launch {
            val id = dataStore.userIdFlow.first()
            if (!id.isNullOrBlank()) {
                val usuarioExiste = usuarioRepository.existeUsuario(id)
                if (!usuarioExiste) dataStore.logout()
            } else {
                dataStore.logout()
            }
            _sesionVerificada.value = true
        }
    }

    fun login(correo: String, contrasena: String) {
        if (correo.isBlank() || contrasena.isBlank()) {
            _authState.value = AuthState.Error("Por favor, llena todos los campos")
            return
        }

        _authState.value = AuthState.Loading

        auth.signInWithEmailAndPassword(correo, contrasena)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = task.result?.user?.uid ?: ""
                    viewModelScope.launch {
                        dataStore.saveSession(uid)
                        _authState.value = AuthState.Success(uid)
                    }
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Error al iniciar sesión")
                }
            }
    }

    fun register(nombre: String, correo: String, contrasena: String, fechaNacimiento: Long, genero: String) {
        if (correo.isBlank() || contrasena.isBlank() || nombre.isBlank()) {
            _authState.value = AuthState.Error("Por favor llena todos los campos obligatorios")
            return
        }

        _authState.value = AuthState.Loading

        auth.createUserWithEmailAndPassword(correo, contrasena)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = task.result?.user?.uid ?: ""

                    val nuevoUsuario = Usuario(
                        idUsuario = uid,
                        nombre = nombre,
                        correo = correo,
                        hashContrasena = contrasena,
                        fechaNacimiento = fechaNacimiento,
                        genero = genero,
                        fotoPerfil = null,
                        fechaRegistro = System.currentTimeMillis()
                    )

                    viewModelScope.launch {
                        usuarioRepository.insertarUsuario(nuevoUsuario)
                        dataStore.saveSession(uid)
                        _authState.value = AuthState.Success(uid)
                    }
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Error al registrar usuario")
                }
            }
    }

    fun loginConBiometria(userId: String) {
        viewModelScope.launch {
            try {
                val usuario = usuarioRepository.obtenerUsuarioPorId(userId).first()
                if (usuario != null) {
                    dataStore.saveSession(userId)
                    _authState.value = AuthState.Success(userId)
                } else {
                    _authState.value = AuthState.Error("No se encontró el usuario")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Error de autenticación: ${e.message}")
            }
        }
    }

    fun logout() {
        auth.signOut()
        viewModelScope.launch {
            dataStore.logout()
            _authState.value = AuthState.Idle
        }
    }

    fun resetState() { _authState.value = AuthState.Idle }

    val usuarioRecordado: StateFlow<Usuario?> = dataStore.userIdFlow
        .flatMapLatest { id ->
            if (!id.isNullOrBlank()) usuarioRepository.obtenerUsuarioPorId(id) else flowOf(null)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun desvincularCuenta() {
        auth.signOut()
        viewModelScope.launch { dataStore.clearRememberedUser() }
    }
}