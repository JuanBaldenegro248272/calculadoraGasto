package LosPrimos.Durango.calculadoragastos.ui.screens

import LosPrimos.Durango.calculadoragastos.navigation.Screen
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import LosPrimos.Durango.calculadoragastos.ui.theme.*
import LosPrimos.Durango.calculadoragastos.ui.components.*
import LosPrimos.Durango.calculadoragastos.viewModel.GastoViewModel
import LosPrimos.Durango.calculadoragastos.viewModel.IngresoViewModel
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    gastoViewModel: GastoViewModel,
    ingresoViewModel: IngresoViewModel,
    onNavigate: (String) -> Unit,
) {
    val usuarioActualId by ingresoViewModel.usuarioActualId.collectAsState()
    val ingresos by ingresoViewModel.obtenerIngresosPorUsuario(usuarioActualId).collectAsState(initial = emptyList())
    val gastos by gastoViewModel.obtenerGastosPorUsuario(usuarioActualId).collectAsState(initial = emptyList())
    var isGastosSelected by remember { mutableStateOf(true) }
    var mesSeleccionado by remember { mutableStateOf("Abril") }
    var showFabMenu by remember { mutableStateOf(false) }
    val isOffline = true
    var categoriaSeleccionada by remember { mutableStateOf("Todas") }


    Scaffold(
        bottomBar = {
            SpentBottomNavigation(
                currentRoute = "home",
                onNavigate = { onNavigate(it) }
            )
        },
        floatingActionButton = {
            Column(horizontalAlignment = Alignment.End) {
                AnimatedVisibility(
                    visible = showFabMenu,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { 50 }),
                    exit = fadeOut() + slideOutVertically(targetOffsetY = { 50 })
                ) {
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        ExtendedFloatingActionButton(
                            onClick = { showFabMenu = false
                                onNavigate(Screen.AgregarIngreso.route)},
                            containerColor = TealDark,
                            contentColor = Color.White,
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            Icon(Icons.Default.ArrowUpward, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Ingreso", fontWeight = FontWeight.Bold)
                        }

                        ExtendedFloatingActionButton(
                            onClick = { showFabMenu = false
                                onNavigate(Screen.AgregarGasto.createRoute())}, //es create route porque no lleva id de algun grupo
                            containerColor = MagentaPink,
                            contentColor = Color.White
                        ) {
                            Icon(Icons.Default.ArrowDownward, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Gasto", fontWeight = FontWeight.Bold)
                        }
                    }
                }




                FloatingActionButton(
                    onClick = { showFabMenu = !showFabMenu },
                    containerColor = TealDark,
                    contentColor = Color.White
                ) {
                    Icon(
                        imageVector = if (showFabMenu) Icons.Default.Close else Icons.Default.Add,
                        contentDescription = "Añadir transacción"
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MainGradient)
                .padding(paddingValues)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (isOffline) {
                        OfflineStatusBar()
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Inicio", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

                    }
                }

                val totalIngresos = ingresos.sumOf { it.monto }
                val totalGastos = gastos.sumOf { it.monto }

                BalanceSummarySection(
                    ingresos = totalIngresos,
                    gastos = totalGastos,
                    balance = totalIngresos - totalGastos,
                    presupuestoUtilizado = 0.716f,
                    textoPresupuesto = "71.6% del presupuesto utilizado ($2,000 / $10,000)"
                )

                Spacer(modifier = Modifier.height(24.dp))

                BottomRoundedSurface {
                    Column(modifier = Modifier.fillMaxSize()) {

                        TransactionsFilterSection(
                            isGastosSelected = isGastosSelected,
                            onGastosClick = {
                                isGastosSelected = true
                                categoriaSeleccionada = "Todas"
                            },
                            onIngresosClick = { isGastosSelected = false },
                            mesSeleccionado = mesSeleccionado,
                            onMesSeleccionado = { mesSeleccionado = it },
                            categoriaSeleccionada = categoriaSeleccionada,
                            onCategoriaSeleccionada = { categoriaSeleccionada = it }
                        )

                        LazyColumn(
                            contentPadding = PaddingValues(bottom = 80.dp)
                        ) {


                            if (isOffline) {
                                item {
                                    OfflineListWarning()
                                }
                            }

                            if (isGastosSelected) {
                                item {
                                    gastos.forEach { gasto ->
                                        TransactionItem(
                                            gasto.descripcion,
                                            formatoFecha(gasto.fecha),
                                            gasto.monto,
                                            true
                                            )
                                        }
                                }
                            } else {
                                item {
                                    ingresos.forEach { ingreso ->
                                        TransactionItem(
                                            ingreso.descripcion,
                                            formatoFecha(ingreso.fecha),
                                                ingreso.monto,
                                            false
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

@RequiresApi(Build.VERSION_CODES.O)
fun formatoFecha(fecha: Long): String{
    val formato = DateTimeFormatter.ofPattern("dd MMM yyyy")
    return Instant.ofEpochMilli(fecha).atZone(ZoneId.systemDefault()).toLocalDate().format(formato)
}


