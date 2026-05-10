package LosPrimos.Durango.calculadoragastos

import LosPrimos.Durango.calculadoragastos.data.DataStoreManager
import LosPrimos.Durango.calculadoragastos.data.SpentDatabase
import LosPrimos.Durango.calculadoragastos.data.repositories.GastoRepository
import LosPrimos.Durango.calculadoragastos.data.repositories.UsuarioRepository
import LosPrimos.Durango.calculadoragastos.navigation.AppNavigationController
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import LosPrimos.Durango.calculadoragastos.ui.theme.CalculadoraGastosTheme
import LosPrimos.Durango.calculadoragastos.viewModel.AppViewModelFactory
import LosPrimos.Durango.calculadoragastos.viewModel.AuthViewModel
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = SpentDatabase.getDatabase(applicationContext)
        val usuarioRepository = UsuarioRepository(database.usuarioDao())
        val gastoRepository = GastoRepository(database.gastoDao())
        val dataStoreManager = DataStoreManager(applicationContext)

        val appViewModelFactory = AppViewModelFactory(
            gastoRepository = gastoRepository,
            usuarioRepository = usuarioRepository,
            dataStoreManager = dataStoreManager,
        )

        setContent {
            CalculadoraGastosTheme {
                val navController = rememberNavController()

                val authViewModel: AuthViewModel = viewModel(factory = appViewModelFactory)

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigationController(
                        navController = navController,
                        authViewModel = authViewModel,
                        factory = appViewModelFactory
                    )
                }
            }
        }
    }
}