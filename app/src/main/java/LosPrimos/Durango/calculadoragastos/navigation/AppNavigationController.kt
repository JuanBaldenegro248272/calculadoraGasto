package LosPrimos.Durango.calculadoragastos.navigation

import LosPrimos.Durango.calculadoragastos.data.DataStoreManager
import LosPrimos.Durango.calculadoragastos.data.SpentDatabase
import LosPrimos.Durango.calculadoragastos.data.repositories.GastoRepository
import LosPrimos.Durango.calculadoragastos.data.repositories.PresupuestoRepository
import LosPrimos.Durango.calculadoragastos.data.repositories.UsuarioRepository
import LosPrimos.Durango.calculadoragastos.ui.screens.AgregarGastoFijoScreen
import LosPrimos.Durango.calculadoragastos.ui.screens.AgregarGastoGrupoScreen
import LosPrimos.Durango.calculadoragastos.ui.screens.HomeScreen
import LosPrimos.Durango.calculadoragastos.ui.screens.LoginScreen
import LosPrimos.Durango.calculadoragastos.ui.screens.RegisterScreen
import LosPrimos.Durango.calculadoragastos.ui.screens.AgregarGastoScreen
import LosPrimos.Durango.calculadoragastos.ui.screens.AgregarIngresoFijoScreen
import LosPrimos.Durango.calculadoragastos.ui.screens.AgregarIngresoScreen
import LosPrimos.Durango.calculadoragastos.ui.screens.DetalleGrupoScreen
import LosPrimos.Durango.calculadoragastos.ui.screens.GraficasScreen
import LosPrimos.Durango.calculadoragastos.ui.screens.GruposScreen
import LosPrimos.Durango.calculadoragastos.ui.screens.PerfilScreen
import LosPrimos.Durango.calculadoragastos.ui.screens.PresupuestosScreen
import LosPrimos.Durango.calculadoragastos.ui.theme.TealDark
import LosPrimos.Durango.calculadoragastos.viewModel.AppViewModelFactory

import LosPrimos.Durango.calculadoragastos.viewModel.AuthViewModel
import LosPrimos.Durango.calculadoragastos.viewModel.CategoriaViewModel
import LosPrimos.Durango.calculadoragastos.viewModel.GastoGrupoViewModel
import LosPrimos.Durango.calculadoragastos.viewModel.GastoViewModel
import LosPrimos.Durango.calculadoragastos.viewModel.GrupoViewModel
import LosPrimos.Durango.calculadoragastos.viewModel.IngresoViewModel
import LosPrimos.Durango.calculadoragastos.viewModel.PerfilViewModel
import LosPrimos.Durango.calculadoragastos.viewModel.PresupuestoViewModel
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
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
        fun createRoute(grupoId: String) = "detalleGrupo/$grupoId"
    }

    object AgregarGasto : Screen("agregarGasto?grupoId={grupoId}") {
        fun createRoute(grupoId: String? = null): String {
            return if (grupoId != null) {
                "agregarGasto?grupoId=$grupoId"
            } else {
                "agregarGasto"
            }
        }
    }

    object AgregarIngreso : Screen("agregarIngreso")
    object AgregarTarjeta : Screen("agregarTarjeta")

    //formularios de creacion para transacciones fijas
    object AgregarIngresoFijo : Screen("agregarIngresoFijo")
    object AgregarGastoFijo : Screen("agregarGastoFijo")

    //formularios de edicion de transacciones fijas
    object EditarGastoFijo : Screen("editarGastoFijo/{idGasto}") {
        fun createRoute(idGasto: String) = "editarGastoFijo/$idGasto"
    }

    object EditarIngresoFijo : Screen("editarIngresoFijo/{idIngreso}") {
        fun createRoute(idIngreso: String) = "editarIngresoFijo/$idIngreso"
    }

    object EditarGasto : Screen("editarGasto/{idGasto}") {
        fun createRoute(idGasto: String) = "editarGasto/$idGasto"
    }

    object EditarIngreso : Screen("editarIngreso/{idIngreso}") {
        fun createRoute(idIngreso: String) = "editarIngreso/$idIngreso"
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigationController(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    factory: AppViewModelFactory

) {


    val loggedIn by authViewModel.isLoggedIn.collectAsState()
    val context = LocalContext.current
    val database = SpentDatabase.getDatabase(context)
    val gastoRepository = GastoRepository(database.gastoDao())
    val dataStoreManager = remember { DataStoreManager(context) }
    val sesionVerificada by authViewModel.sesionVerificada.collectAsState()

    LaunchedEffect(Unit) {
        authViewModel.verificarSesion()
    }

    if (!sesionVerificada) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = TealDark)
        }
        return
    }


    val gastoViewModel: GastoViewModel = viewModel(factory = factory)
    val ingresoViewModel: IngresoViewModel = viewModel(factory = factory)
    val categoriaViewModel: CategoriaViewModel = viewModel(factory = factory)
    val presupuestoViewModel: PresupuestoViewModel = viewModel(factory = factory)
    val grupoViewModel: GrupoViewModel = viewModel(factory = factory)
    val gastoGrupoViewModel: GastoGrupoViewModel = viewModel(factory = factory)

    LaunchedEffect(Unit) {
        categoriaViewModel.insertarCategorias()
    }
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = authViewModel,
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
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
                gastoViewModel = gastoViewModel,
                ingresoViewModel = ingresoViewModel,
                categoriaViewModel = categoriaViewModel,
                presupuestoViewModel = presupuestoViewModel,
                onNavigate = { rutaDestino ->
                    navController.navigate(rutaDestino) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        composable(Screen.Grupos.route) {
            GruposScreen(
                onNavigate = { ruta ->
                    navController.navigate(ruta) {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                grupoViewModel = grupoViewModel
            )
        }
        composable("graficas") {
            GraficasScreen(
                onNavigate = { navController.navigate(it) },
                gastoViewModel = gastoViewModel,
                ingresoViewModel = ingresoViewModel,
                presupuestoViewModel = presupuestoViewModel,
                categoriaViewModel = categoriaViewModel
            )
        }


        composable(Screen.Presupuestos.route) {
            PresupuestosScreen(
                presupuestoViewModel = presupuestoViewModel,
                gastoViewModel = gastoViewModel,
                ingresoViewModel = ingresoViewModel,
                onNavigate = { ruta ->
                    navController.navigate(ruta) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        composable(Screen.Perfil.route) {
            val perfilViewModel: PerfilViewModel = viewModel(factory = factory)
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
                },
                viewModel = perfilViewModel
            )
        }

        // Formulario de Gasto (Individual o Grupo)
        composable(
            route = Screen.AgregarGasto.route,
            arguments = listOf(navArgument("grupoId") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val grupoId = backStackEntry.arguments?.getString("grupoId")
            AgregarGastoScreen(
                onBack = { navController.popBackStack() },
                gastoviewModel = gastoViewModel,
                categoriaViewModel = categoriaViewModel

            )
        }

        composable(
            route = Screen.EditarGasto.route,
            arguments = listOf(navArgument("idGasto") { type = NavType.StringType })
        ) { backStackEntry ->
            val idGasto = backStackEntry.arguments?.getString("idGasto")?.toIntOrNull() ?: return@composable
            AgregarGastoScreen(
                onBack = { navController.popBackStack() },
                gastoviewModel = gastoViewModel,
                categoriaViewModel = categoriaViewModel,
                idGastoEditar = idGasto
            )
        }

        composable(Screen.AgregarIngreso.route) {
            AgregarIngresoScreen(
                onBack = { navController.popBackStack() },
                ingresoViewModel = ingresoViewModel
            )
        }

        composable(Screen.AgregarIngresoFijo.route) {
            AgregarIngresoFijoScreen(
                onBack = { navController.popBackStack() },
                ingresoViewModel = ingresoViewModel
            )
        }

        composable(Screen.AgregarGastoFijo.route) {
            AgregarGastoFijoScreen(
                onBack = { navController.popBackStack() },
                gastoViewModel = gastoViewModel
            )
        }

        composable(
            route = Screen.EditarGastoFijo.route,
            arguments = listOf(navArgument("idGasto") { type = NavType.StringType })
        ) { backStackEntry ->
            val idGasto = backStackEntry.arguments?.getString("idGasto")?.toIntOrNull() ?: return@composable

            AgregarGastoFijoScreen(
                onBack = { navController.popBackStack() },
                gastoViewModel = gastoViewModel,
                idGastoEditar = idGasto
            )
        }

        composable(
            route = Screen.EditarIngresoFijo.route,
            arguments = listOf(navArgument("idIngreso") { type = NavType.StringType })
        ) { backStackEntry ->
            val idIngreso = backStackEntry.arguments?.getString("idIngreso")?.toIntOrNull() ?: return@composable

            AgregarIngresoFijoScreen(
                onBack = { navController.popBackStack() },
                ingresoViewModel = ingresoViewModel,
                idIngresoEditar = idIngreso
            )
        }

        composable(
            route = Screen.EditarIngreso.route,
            arguments = listOf(navArgument("idIngreso") { type = NavType.StringType })
        ) { backStackEntry ->
            val idIngreso = backStackEntry.arguments?.getString("idIngreso")?.toIntOrNull() ?: return@composable
            AgregarIngresoScreen(
                onBack = { navController.popBackStack() },
                ingresoViewModel = ingresoViewModel,
                idIngresoEditar = idIngreso
            )
        }

        composable(
            route = Screen.DetalleGrupo.route,
            arguments = listOf(navArgument("grupoId") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val grupoId = backStackEntry.arguments?.getString("grupoId") ?: return@composable
            DetalleGrupoScreen(
                idGrupo             = grupoId,
                onNavigate          = { navController.navigate(it) },
                onBack              = { navController.popBackStack() },
                grupoViewModel      = grupoViewModel,
                gastoGrupoViewModel = gastoGrupoViewModel
            )
        }

        composable("agregarGastoGrupo/{idGrupo}") { backStackEntry ->
            val idGrupo = backStackEntry.arguments?.getString("idGrupo") ?: return@composable
            AgregarGastoGrupoScreen(idGrupo = idGrupo, onBack = { navController.popBackStack() }, gastoGrupoViewModel = gastoGrupoViewModel)
        }

        composable("editarGastoGrupo/{idGrupo}/{idGastoGrupo}") { backStackEntry ->
            val idGrupo      = backStackEntry.arguments?.getString("idGrupo") ?: return@composable
            val idGastoEditar = backStackEntry.arguments?.getString("idGastoGrupo")
            AgregarGastoGrupoScreen(idGrupo = idGrupo, idGastoEditar = idGastoEditar, onBack = { navController.popBackStack() }, gastoGrupoViewModel = gastoGrupoViewModel)
        }
    }
}
