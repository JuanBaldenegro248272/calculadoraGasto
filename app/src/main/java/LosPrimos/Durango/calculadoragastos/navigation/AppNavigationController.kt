package LosPrimos.Durango.calculadoragastos.navigation

import LosPrimos.Durango.calculadoragastos.ui.screens.LoginScreen
import LosPrimos.Durango.calculadoragastos.ui.screens.RegisterScreen
import LosPrimos.Durango.calculadoragastos.ui.screens.ResumeScreen
import LosPrimos.Durango.calculadoragastos.viewModel.AuthViewModel
import android.R.attr.defaultValue
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
    object MenuPrincipal : Screen("menuPrincipal")
    object Perfil : Screen("perfil")
    object Graficas : Screen("graficas")
    object Presupuestos : Screen("presupuestos")
    object Grupos : Screen("grupos")

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

@Composable
fun AppNavigationController(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    val loggedIn by authViewModel.isLoggedIn.collectAsState()

    NavHost(
        navController = navController,
        startDestination = if (loggedIn) Screen.MenuPrincipal.route else Screen.Login.route
    ) {


        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = authViewModel,
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onLoginSuccess = {
                    navController.navigate(Screen.MenuPrincipal.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.MenuPrincipal.route) {
            Text("¡Bienvenido al Menú Principal!", modifier = Modifier.fillMaxSize())
        }



        composable(Screen.Register.route) {
            RegisterScreen(
                navController = navController,
                viewModel = authViewModel
            )
        }

        // navegacion inferior
        composable(Screen.MenuPrincipal.route) {
            ResumeScreen(
                onLogoutClick = {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Grupos.route) {
            //GruposScreen(navController)
        }

        composable(Screen.Graficas.route) {
            //GraficasScreen(navController)
        }

        composable(Screen.Presupuestos.route) {
            //PresupuestosScreen(navController)
        }

        composable(Screen.Perfil.route) {
            //PerfilScreen(navController, authViewModel)
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
            //GastoScreen(grupoId, navController)
        }

        // Formulario de Ingreso y Tarjeta
        //composable(Screen.AgregarIngreso.route) { IngresoScreen(navController) }
        //composable(Screen.AgregarTarjeta.route) { TarjetaScreen(navController) }
    }
}