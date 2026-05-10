package LosPrimos.Durango.calculadoragastos.navigation

import LosPrimos.Durango.calculadoragastos.ui.screens.HomeScreen
import LosPrimos.Durango.calculadoragastos.ui.screens.LoginScreen
import LosPrimos.Durango.calculadoragastos.ui.screens.RegisterScreen
import LosPrimos.Durango.calculadoragastos.ui.screens.AgregarGastoScreen
import LosPrimos.Durango.calculadoragastos.ui.screens.AgregarIngresoScreen
import LosPrimos.Durango.calculadoragastos.ui.screens.GraficasScreen
import LosPrimos.Durango.calculadoragastos.ui.screens.GruposScreen
import LosPrimos.Durango.calculadoragastos.ui.screens.PerfilScreen

import LosPrimos.Durango.calculadoragastos.viewModel.AuthViewModel
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Perfil : Screen("perfil")
    object Graficas : Screen("graficas")
    object Presupuestos : Screen("presupuestos")
    object Grupos : Screen("grupos")
    object Home : Screen("home")

    object DetalleGrupo : Screen("detalleGrupo/{grupoId}") {
        fun createRoute(grupoId: Int) = "detalleGrupo/$grupoId"
    }

    object AgregarGasto : Screen("agregarGasto?grupoId={grupoId}") {
        fun createRoute(grupoId: Int? = null): String {
            return if (grupoId != null) {
                "agregarGasto?grupoId=$grupoId"
            } else {
                "agregarGasto"
            }
        }
    }

    object AgregarIngreso : Screen("agregarIngreso")
    object AgregarTarjeta : Screen("agregarTarjeta")


}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigationController(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    val loggedIn by authViewModel.isLoggedIn.collectAsState()

    NavHost(
        navController = navController,
        startDestination = if (loggedIn) Screen.Home.route else Screen.Login.route
    ) {

        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = authViewModel,
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(navController = navController, viewModel = authViewModel)
        }

        // navegacion inferior
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigate = { rutaDestino ->
                    navController.navigate(rutaDestino) {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        }


        composable(Screen.Grupos.route) {
            GruposScreen(
                onNavigate = { ruta ->
                    navController.navigate(ruta) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        composable(Screen.Graficas.route) {
            GraficasScreen(
                onNavigate = { rutaDestino ->
                    navController.navigate(rutaDestino) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

//        composable(Screen.Presupuestos.route) {
//            //PresupuestosScreen(navController)
//        }



        composable(Screen.Perfil.route) {
            PerfilScreen(
                onNavigate = { ruta ->
                    navController.navigate(ruta) {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onLogoutClick = {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // Detalle de Grupo
        composable(
            route = Screen.DetalleGrupo.route,
            arguments = listOf(navArgument("grupoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("grupoId") ?: 0
            //DetalleGrupoScreen(id, navController)
        }

        // Formulario de Gasto (Individual o Grupo)
        composable(
            route = Screen.AgregarGasto.route,
            arguments = listOf(navArgument("grupoId") {
                type = NavType.IntType
                defaultValue = -1 // -1 significa gasto individual
            })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("grupoId")
            val grupoId = if (id == -1) null else id
            AgregarGastoScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.AgregarIngreso.route) {
            AgregarIngresoScreen(onBack = { navController.popBackStack() })
        }

        // Formulario de Tarjeta
        //composable(Screen.AgregarTarjeta.route) { TarjetaScreen(navController) }
    }
}
