package LosPrimos.Durango.calculadoragastos.viewModel

import LosPrimos.Durango.calculadoragastos.data.DataStoreManager
import LosPrimos.Durango.calculadoragastos.data.repositories.GastoRepository
import LosPrimos.Durango.calculadoragastos.data.repositories.UsuarioRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AppViewModelFactory(
    private val gastoRepository: GastoRepository,
    private val usuarioRepository: UsuarioRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GastoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GastoViewModel(gastoRepository, dataStoreManager) as T
        }
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(usuarioRepository, dataStoreManager) as T
        }
        if (modelClass.isAssignableFrom(PerfilViewModel::class.java)) {
            return PerfilViewModel(usuarioRepository, dataStoreManager) as T
        }
        throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}