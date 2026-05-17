package LosPrimos.Durango.calculadoragastos.viewModel

import LosPrimos.Durango.calculadoragastos.data.DataStoreManager
import LosPrimos.Durango.calculadoragastos.data.repositories.CategoriaRepository
import LosPrimos.Durango.calculadoragastos.data.repositories.GastoRepository
import LosPrimos.Durango.calculadoragastos.data.repositories.GrupoRepository
import LosPrimos.Durango.calculadoragastos.data.repositories.IngresoRepository
import LosPrimos.Durango.calculadoragastos.data.repositories.PresupuestoRepository
import LosPrimos.Durango.calculadoragastos.data.repositories.UsuarioRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AppViewModelFactory(
    private val gastoRepository: GastoRepository,
    private val usuarioRepository: UsuarioRepository,
    private val dataStoreManager: DataStoreManager,
    private val ingresoRepository: IngresoRepository,
    private val categoriaRepository: CategoriaRepository,
    private val presupuestoRepository: PresupuestoRepository,
    private val grupoRepository: GrupoRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GastoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GastoViewModel(gastoRepository, dataStoreManager) as T
        }
        if (modelClass.isAssignableFrom(IngresoViewModel::class.java)){
            return IngresoViewModel(ingresoRepository, dataStoreManager) as T
        }
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(usuarioRepository, dataStoreManager) as T
        }
        if (modelClass.isAssignableFrom(PerfilViewModel::class.java)) {
            return PerfilViewModel(usuarioRepository, dataStoreManager) as T
        }
        if (modelClass.isAssignableFrom(CategoriaViewModel::class.java)){
            return CategoriaViewModel(categoriaRepository) as T
        }
        if (modelClass.isAssignableFrom(PresupuestoViewModel::class.java)){
            return PresupuestoViewModel(presupuestoRepository, dataStoreManager) as T
        }
        if (modelClass.isAssignableFrom(GrupoViewModel::class.java)){
            return GrupoViewModel(grupoRepository, dataStoreManager) as T
        }
        if (modelClass.isAssignableFrom(GastoGrupoViewModel::class.java)){
            return GastoGrupoViewModel() as T
        }
        throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}
