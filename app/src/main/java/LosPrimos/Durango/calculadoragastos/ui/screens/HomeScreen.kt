package LosPrimos.Durango.calculadoragastos.ui.screens

import LosPrimos.Durango.calculadoragastos.data.entities.Gasto
import LosPrimos.Durango.calculadoragastos.data.entities.Ingreso
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
import LosPrimos.Durango.calculadoragastos.utils.SwipeToReveal
import LosPrimos.Durango.calculadoragastos.viewModel.CategoriaViewModel
import LosPrimos.Durango.calculadoragastos.viewModel.GastoViewModel
import LosPrimos.Durango.calculadoragastos.viewModel.IngresoViewModel
import LosPrimos.Durango.calculadoragastos.viewModel.PresupuestoViewModel
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import java.text.NumberFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    gastoViewModel: GastoViewModel,
    ingresoViewModel: IngresoViewModel,
    presupuestoViewModel: PresupuestoViewModel,
    onNavigate: (String) -> Unit,
    categoriaViewModel: CategoriaViewModel
) {
    val usuarioActualId by ingresoViewModel.usuarioActualId.collectAsState()
    val ingresos by ingresoViewModel.obtenerIngresosPorUsuario(usuarioActualId)
        .collectAsState(initial = emptyList())
    val gastos by gastoViewModel.obtenerGastosPorUsuario(usuarioActualId)
        .collectAsState(initial = emptyList())
    val categorias by categoriaViewModel.obtenerCategorias().collectAsState(initial = emptyList())
    val nombresCategorias = listOf("Todas") + categorias.map { it.nombre }
    val presupuestos by presupuestoViewModel.obtenerPresupuesto(usuarioActualId ?: 0).collectAsState(initial = emptyList())

    val mesesMapReverso = mapOf(
        1 to "Enero", 2 to "Febrero", 3 to "Marzo", 4 to "Abril",
        5 to "Mayo", 6 to "Junio", 7 to "Julio", 8 to "Agosto",
        9 to "Septiembre", 10 to "Octubre", 11 to "Noviembre", 12 to "Diciembre"
    )
    val mesActualTexto = remember { mesesMapReverso[LocalDate.now().monthValue] ?: "Todos" }

    var mesSeleccionado by remember { mutableStateOf(mesActualTexto) }


    var isGastosSelected by remember { mutableStateOf(true) }
    var showFabMenu by remember { mutableStateOf(false) }
    val isOffline = true
    var categoriaSeleccionada by remember { mutableStateOf("Todas") }

    var gastoSeleccionado by remember { mutableStateOf<Gasto?>(null) }
    var ingresoSeleccionado by remember { mutableStateOf<Ingreso?>(null) }


    val mesesMap = mapOf(
        "Enero" to 1, "Febrero" to 2, "Marzo" to 3, "Abril" to 4,
        "Mayo" to 5, "Junio" to 6, "Julio" to 7, "Agosto" to 8,
        "Septiembre" to 9, "Octubre" to 10, "Noviembre" to 11, "Diciembre" to 12
    )


    val gastosFiltrados = gastos.filter { gasto ->
        val date = Instant.ofEpochMilli(gasto.fecha).atZone(ZoneId.systemDefault()).toLocalDate()
        val matchMes =
            if (mesSeleccionado == "Todos") true else date.monthValue == mesesMap[mesSeleccionado]

        val nombreCat = categorias.find { it.idCategoria == gasto.idCategoria }?.nombre ?: "Otros"
        val matchCat =
            if (categoriaSeleccionada == "Todas") true else nombreCat == categoriaSeleccionada

        matchMes && matchCat
    }

    val ingresosFiltrados = ingresos.filter { ingreso ->
        val date = Instant.ofEpochMilli(ingreso.fecha).atZone(ZoneId.systemDefault()).toLocalDate()
        if (mesSeleccionado == "Todos") true else date.monthValue == mesesMap[mesSeleccionado]
    }


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
                            onClick = {
                                showFabMenu = false
                                onNavigate(Screen.AgregarIngreso.route)
                            },
                            containerColor = TealDark,
                            contentColor = Color.White,
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            Icon(Icons.Default.ArrowUpward, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Ingreso", fontWeight = FontWeight.Bold)
                        }

                        ExtendedFloatingActionButton(
                            onClick = {
                                showFabMenu = false
                                onNavigate(Screen.AgregarGasto.createRoute())
                            },
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
                        Text(
                            "Inicio",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )

                    }
                }

                val totalIngresos = ingresos.sumOf { it.monto }
                val totalGastos = gastos.sumOf { it.monto }
                val presupuestoMensual = presupuestos.sumOf { it.monto }
                val proporcionPresupuesto = if (presupuestoMensual > 0) {
                    (totalGastos / presupuestoMensual).toFloat()
                } else {
                    0f
                }

                val progresoBarra = proporcionPresupuesto.coerceIn(0f, 1f)
                val porcentajeTexto = String.format("%.1f", proporcionPresupuesto * 100)
                val formatoDinero = NumberFormat.getCurrencyInstance()
                val gastosFormateado = formatoDinero.format(totalGastos)
                val presupuestoFormateado = formatoDinero.format(presupuestoMensual)

                BalanceSummarySection(
                    ingresos = totalIngresos,
                    gastos = totalGastos,
                    balance = totalIngresos - totalGastos,
                    presupuestoUtilizado = progresoBarra,
                    textoPresupuesto = "$porcentajeTexto% del presupuesto utilizado ($gastosFormateado / $presupuestoFormateado)"
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
                            onCategoriaSeleccionada = { categoriaSeleccionada = it },
                            listaCategoriasNombres = nombresCategorias
                        )

                        LazyColumn(
                            contentPadding = PaddingValues(bottom = 80.dp)
                        ) {

                            if (isOffline) {
                                item { OfflineListWarning() }
                            }

                            if (isGastosSelected) {
                                val gastosAgrupados = gastosFiltrados.groupBy { gasto ->
                                    categorias.find { it.idCategoria == gasto.idCategoria }?.nombre
                                        ?: "Otros"
                                }

                                gastosAgrupados.forEach { (nombreCategoria, listaGastos) ->
                                    item {
                                        CategoryGroup(
                                            nombreCategoria = nombreCategoria,
                                            iconoCategoria = null
                                        ) {
                                            listaGastos.forEach { gasto ->
                                                SwipeToReveal(
                                                    onEdit = {
                                                        onNavigate(
                                                            Screen.EditarGasto.createRoute(
                                                                gasto.idGasto
                                                            )
                                                        )
                                                    },
                                                    onDelete = {
                                                        gastoViewModel.eliminarGasto(gasto)
                                                    }
                                                ) {
                                                    Box(modifier = Modifier.clickable { gastoSeleccionado = gasto }) {
                                                    TransactionItem(
                                                        titulo = gasto.descripcion,
                                                        fecha = formatoFecha(gasto.fecha),
                                                        monto = gasto.monto,
                                                        isGasto = true
                                                    )
                                                }
                                                }
                                            }
                                        }
                                    }
                                }

                                if (gastosFiltrados.isEmpty()) {
                                    item {
                                        Text(
                                            "No hay gastos en este mes/categoría",
                                            modifier = Modifier.padding(16.dp)
                                        )
                                    }
                                }

                            } else {
                                item {
                                    CategoryGroup("Ingresos", Icons.Default.ArrowUpward) {
                                        ingresosFiltrados.forEach { ingreso ->
                                            SwipeToReveal(
                                                onEdit = {
                                                    onNavigate(
                                                        Screen.EditarIngreso.createRoute(
                                                            ingreso.idIngreso
                                                        )
                                                    )
                                                },
                                                onDelete = {
                                                    ingresoViewModel.eliminarIngreso(ingreso)
                                                }
                                            ) {
                                                Box(modifier = Modifier.clickable {
                                                    ingresoSeleccionado = ingreso
                                                }) {
                                                    TransactionItem(
                                                        titulo = ingreso.descripcion,
                                                        fecha = formatoFecha(ingreso.fecha),
                                                        monto = ingreso.monto,
                                                        isGasto = false
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
        }

        if (gastoSeleccionado != null) {
            DetalleGastoDialog(
                gasto = gastoSeleccionado!!,
                categoriaNombre = categorias.find { it.idCategoria == gastoSeleccionado!!.idCategoria }?.nombre ?: "Otros",
                onDismiss = { gastoSeleccionado = null }
            )
        }

        if (ingresoSeleccionado != null) {
            DetalleIngresoDialog(
                ingreso = ingresoSeleccionado!!,
                onDismiss = { ingresoSeleccionado = null }
            )
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
fun formatoFecha(fecha: Long): String{
    val formato = DateTimeFormatter.ofPattern("dd MMM yyyy")
    return Instant.ofEpochMilli(fecha).atZone(ZoneId.systemDefault()).toLocalDate().format(formato)
}


