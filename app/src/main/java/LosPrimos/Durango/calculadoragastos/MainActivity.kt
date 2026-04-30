package LosPrimos.Durango.calculadoragastos

import LosPrimos.Durango.calculadoragastos.data.DataStoreManager
import LosPrimos.Durango.calculadoragastos.data.SpentDatabase
import LosPrimos.Durango.calculadoragastos.data.repositories.UsuarioRepository
import LosPrimos.Durango.calculadoragastos.navigation.AppNavigationController
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import LosPrimos.Durango.calculadoragastos.ui.theme.CalculadoraGastosTheme
import LosPrimos.Durango.calculadoragastos.viewModel.AuthViewModel
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import kotlin.getValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = SpentDatabase.getDatabase(applicationContext)
        val usuarioRepository = UsuarioRepository(database.usuarioDao())
        val dataStoreManager = DataStoreManager(applicationContext)

        val authViewModelFactory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
                    return AuthViewModel(usuarioRepository, dataStoreManager) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }

        setContent {
            CalculadoraGastosTheme {

                val navController = rememberNavController()
                val authViewModel: AuthViewModel = viewModel(factory = authViewModelFactory)

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigationController(
                        navController = navController,
                        authViewModel = authViewModel
                    )
                }
            }
        }
    }
}
