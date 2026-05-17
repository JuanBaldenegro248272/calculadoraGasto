package LosPrimos.Durango.calculadoragastos.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import LosPrimos.Durango.calculadoragastos.data.entities.Gasto
import LosPrimos.Durango.calculadoragastos.data.entities.Ingreso
import LosPrimos.Durango.calculadoragastos.data.entities.Presupuesto
import LosPrimos.Durango.calculadoragastos.navigation.Screen
import LosPrimos.Durango.calculadoragastos.ui.components.*
import LosPrimos.Durango.calculadoragastos.ui.theme.*
import LosPrimos.Durango.calculadoragastos.utils.SwipeToReveal
import LosPrimos.Durango.calculadoragastos.viewModel.GastoViewModel
import LosPrimos.Durango.calculadoragastos.viewModel.IngresoViewModel
import LosPrimos.Durango.calculadoragastos.viewModel.PresupuestoViewModel
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.ui.text.input.KeyboardType
import kotlinx.coroutines.launch
import java.text.NumberFormat
import kotlin.math.abs

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PresupuestosScreen(
    onNavigate: (String) -> Unit,
    presupuestoViewModel: PresupuestoViewModel,
    gastoViewModel: GastoViewModel,
    ingresoViewModel: IngresoViewModel
) {
    val pagerState = rememberPagerState(pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()
    val isFijosSelected = pagerState.currentPage == 0

    var gastoFijoDetalle by remember { mutableStateOf<Gasto?>(null) }
    var ingresoFijoDetalle by remember { mutableStateOf<Ingreso?>(null) }

    val usuarioActualId by presupuestoViewModel.usuarioActualId.collectAsState()

    val usuarioId = usuarioActualId ?: ""

    val gastosFijos by gastoViewModel.obtenerGastosFijosPorUsuario(usuarioId).collectAsState(initial = emptyList())
    val ingresosFijos by ingresoViewModel.obtenerIngresosFijosPorUsuario(usuarioId).collectAsState(initial = emptyList())
    val presupuestos by presupuestoViewModel.obtenerPresupuesto(usuarioId).collectAsState(initial = emptyList())
    val gastos by gastoViewModel.obtenerGastosPorUsuario(usuarioId).collectAsState(initial = emptyList())

    val ingresosFijosTotales = ingresosFijos.sumOf { it.monto }
    val gastosFijosTotales = gastosFijos.sumOf { it.monto }
    val presupuestoBase = ingresosFijosTotales - gastosFijosTotales
    val presupuestoAsignadoTotal = presupuestos.sumOf { it.monto }
    val disponibleParaDistribuir = presupuestoBase - presupuestoAsignadoTotal
    val presupuestoGastado = gastos.sumOf { gasto ->
        if (presupuestos.any { it.idCategoria == gasto.idCategoria }) gasto.monto else 0.0
    }

    val presupuestoVivienda = presupuestos.find { it.idCategoria == 1 }
    val montoPresupuestoVivienda = presupuestoVivienda?.monto ?: 0.0
    val gastadoVivienda = gastos.filter { it.idCategoria == 1 }.sumOf { it.monto }

    val presupuestoEntretenimiento = presupuestos.find { it.idCategoria == 2 }
    val montoPresupuestoEntretenimiento = presupuestoEntretenimiento?.monto ?: 0.0
    val gastadoEntretenimiento = gastos.filter { it.idCategoria == 2 }.sumOf { it.monto }

    val presupuestoTransporte = presupuestos.find { it.idCategoria == 3 }
    val montoPresupuestoTransporte = presupuestoTransporte?.monto ?: 0.0
    val gastadoTransporte = gastos.filter { it.idCategoria == 3 }.sumOf { it.monto }

    val presupuestoAlimentacion = presupuestos.find { it.idCategoria == 4 }
    val montoPresupuestoAlimentacion = presupuestoAlimentacion?.monto ?: 0.0
    val gastadoAlimentacion = gastos.filter { it.idCategoria == 4 }.sumOf { it.monto }

    val presupuestoSalud = presupuestos.find { it.idCategoria == 5 }
    val montoPresupuestoSalud = presupuestoSalud?.monto ?: 0.0
    val gastadoSalud = gastos.filter { it.idCategoria == 5 }.sumOf { it.monto }

    val presupuestoOtros = presupuestos.find { it.idCategoria == 6 }
    val montoPresupuestoOtros = presupuestoOtros?.monto ?: 0.0
    val gastadoOtros = gastos.filter { it.idCategoria == 6 }.sumOf { it.monto }

    val formatoDinero = NumberFormat.getCurrencyInstance()

    Scaffold(
        bottomBar = { SpentBottomNavigation(currentRoute = "presupuestos", onNavigate = { onNavigate(it) }) },
        floatingActionButton = {
            AnimatedVisibility(
                visible = isFijosSelected,
                enter = fadeIn() + slideInVertically(initialOffsetY = { 50 }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { 50 })
            ) {
                ExpandableFixedTransactionFAB(
                    onAddIngresoClick = { onNavigate(Screen.AgregarIngresoFijo.route) },
                    onAddGastoClick = { onNavigate(Screen.AgregarGastoFijo.route) }
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().background(MainGradient).padding(paddingValues)) {
            Column(modifier = Modifier.fillMaxSize()) {

                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Presupuestos", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                }

                PresupuestoSummaryCard(
                    ingresosFijos = ingresosFijosTotales,
                    gastosFijos = gastosFijosTotales,
                    presupuestoUsado = presupuestoGastado,
                    presupuestoTotal = presupuestoBase
                )

                Spacer(modifier = Modifier.height(10.dp))
                DisponibleParaDistribuirBanner(
                    disponible = disponibleParaDistribuir,
                    formateado = formatoDinero.format(abs(disponibleParaDistribuir))
                )
                Spacer(modifier = Modifier.height(16.dp))

                BottomRoundedSurface {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Spacer(modifier = Modifier.height(24.dp))
                        PresupuestosToggle(
                            isFijosSelected = isFijosSelected,
                            onFijosClick = { coroutineScope.launch { pagerState.animateScrollToPage(0, animationSpec = tween(300)) } },
                            onCategoriasClick = { coroutineScope.launch { pagerState.animateScrollToPage(1, animationSpec = tween(300)) } }
                        )

                        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
                            when (page) {
                                0 -> FijosPage(
                                    ingresosFijos = ingresosFijos,
                                    gastosFijos = gastosFijos,
                                    ingresosFijosTotales = ingresosFijosTotales,
                                    gastosFijosTotales = gastosFijosTotales,
                                    formatoDinero = formatoDinero,
                                    onVerDetalle = { ingreso -> ingresoFijoDetalle = ingreso },
                                    onVerDetalleGasto = { gasto -> gastoFijoDetalle = gasto },
                                    onEditarIngreso = { ingreso -> onNavigate(Screen.EditarIngresoFijo.createRoute(ingreso.idIngreso.toString())) },
                                    onEditarGasto = { gasto -> onNavigate(Screen.EditarGastoFijo.createRoute(gasto.idGasto.toString())) },
                                    onEliminarIngreso = { ingresoViewModel.eliminarIngreso(it) },
                                    onEliminarGasto = { gastoViewModel.eliminarGasto(it) }
                                )
                                1 -> CategoriasPage(
                                    usuarioId = usuarioId,
                                    presupuestoViewModel = presupuestoViewModel,
                                    presupuestoVivienda = presupuestoVivienda, montoPresupuestoVivienda = montoPresupuestoVivienda, gastadoVivienda = gastadoVivienda,
                                    presupuestoEntretenimiento = presupuestoEntretenimiento, montoPresupuestoEntretenimiento = montoPresupuestoEntretenimiento, gastadoEntretenimiento = gastadoEntretenimiento,
                                    presupuestoTransporte = presupuestoTransporte, montoPresupuestoTransporte = montoPresupuestoTransporte, gastadoTransporte = gastadoTransporte,
                                    presupuestoAlimentacion = presupuestoAlimentacion, montoPresupuestoAlimentacion = montoPresupuestoAlimentacion, gastadoAlimentacion = gastadoAlimentacion,
                                    presupuestoSalud = presupuestoSalud, montoPresupuestoSalud = montoPresupuestoSalud, gastadoSalud = gastadoSalud,
                                    presupuestoOtros = presupuestoOtros, montoPresupuestoOtros = montoPresupuestoOtros, gastadoOtros = gastadoOtros
                                )
                            }
                        }
                    }
                }
            }

            if (gastoFijoDetalle != null) {
                DetalleGastoFijoDialog(gasto = gastoFijoDetalle!!) { gastoFijoDetalle = null }
            }

            if (ingresoFijoDetalle != null) {
                DetalleIngresoFijoDialog(ingreso = ingresoFijoDetalle!!) { ingresoFijoDetalle = null }
            }
        }
    }
}

@Composable
private fun FijosPage(
    ingresosFijos: List<Ingreso>,
    gastosFijos: List<Gasto>,
    ingresosFijosTotales: Double,
    gastosFijosTotales: Double,
    formatoDinero: NumberFormat,
    onVerDetalle: (Ingreso) -> Unit,
    onVerDetalleGasto: (Gasto) -> Unit,
    onEditarIngreso: (Ingreso) -> Unit,
    onEditarGasto: (Gasto) -> Unit,
    onEliminarIngreso: (Ingreso) -> Unit,
    onEliminarGasto: (Gasto) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = 16.dp, bottom = 120.dp)
    ) {
        if (ingresosFijos.isEmpty()) {
            item {
                Text(
                    text = "No hay ingresos fijos registrados.",
                    modifier = Modifier.padding(horizontal = 32.dp, vertical = 12.dp),
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        } else {
            item {
                ExpandableCategoryItem(
                    nombreCategoria = "Ingresos Fijos (${formatoDinero.format(ingresosFijosTotales)})",
                    icono = Icons.Default.ArrowUpward,
                    expandedByDefault = true
                ) {
                    Column {
                        ingresosFijos.forEach { ingreso ->
                            SwipeToReveal(
                                onEdit = { onEditarIngreso(ingreso) },
                                onDelete = { onEliminarIngreso(ingreso) }
                            ) {
                                Box(modifier = Modifier.clickable { onVerDetalle(ingreso) }) {
                                    TransactionItem(
                                        titulo = ingreso.descripcion ?: "Sin descripción",
                                        fecha = ingreso.lugar?.ifEmpty { "Mensual" } ?: "Mensual",
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

        item { Spacer(modifier = Modifier.height(8.dp)) }

        if (gastosFijos.isEmpty()) {
            item {
                Text(
                    text = "No hay gastos fijos registrados.",
                    modifier = Modifier.padding(horizontal = 32.dp, vertical = 12.dp),
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        } else {
            item {
                ExpandableCategoryItem(
                    nombreCategoria = "Gastos Fijos (${formatoDinero.format(gastosFijosTotales)})",
                    icono = Icons.Default.ArrowDownward,
                    expandedByDefault = true
                ) {
                    Column {
                        gastosFijos.forEach { gasto ->
                            SwipeToReveal(
                                onEdit = { onEditarGasto(gasto) },
                                onDelete = { onEliminarGasto(gasto) }
                            ) {
                                Box(modifier = Modifier.clickable { onVerDetalleGasto(gasto) }) {
                                    TransactionItem(
                                        titulo = gasto.descripcion,
                                        fecha = gasto.lugar.ifEmpty { "Mensual" },
                                        monto = gasto.monto,
                                        isGasto = true
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

@Composable
private fun CategoriasPage(
    usuarioId: String,
    presupuestoViewModel: PresupuestoViewModel,
    presupuestoVivienda: Presupuesto?, montoPresupuestoVivienda: Double, gastadoVivienda: Double,
    presupuestoEntretenimiento: Presupuesto?, montoPresupuestoEntretenimiento: Double, gastadoEntretenimiento: Double,
    presupuestoTransporte: Presupuesto?, montoPresupuestoTransporte: Double, gastadoTransporte: Double,
    presupuestoAlimentacion: Presupuesto?, montoPresupuestoAlimentacion: Double, gastadoAlimentacion: Double,
    presupuestoSalud: Presupuesto?, montoPresupuestoSalud: Double, gastadoSalud: Double,
    presupuestoOtros: Presupuesto?, montoPresupuestoOtros: Double, gastadoOtros: Double
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
    ) {
        item {
            ExpandableCategoryItem(nombreCategoria = "Vivienda", icono = Icons.Default.Home, expandedByDefault = true) {
                CategoryBudgetCard("Vivienda", gastadoVivienda, montoPresupuestoVivienda, true) { monto, alertas ->
                    if (usuarioId.isNotBlank()) presupuestoViewModel.insertarPresupuesto(Presupuesto(presupuestoVivienda?.idPresupuesto ?: 0, monto, 5, 2026, if (alertas) 1 else 0, usuarioId, 1))
                }
            }
        }
        item {
            ExpandableCategoryItem(nombreCategoria = "Transporte", icono = Icons.Default.DirectionsCar) {
                CategoryBudgetCard("Transporte", gastadoTransporte, montoPresupuestoTransporte, true) { monto, alertas ->
                    if (usuarioId.isNotBlank()) presupuestoViewModel.insertarPresupuesto(Presupuesto(presupuestoTransporte?.idPresupuesto ?: 0, monto, 5, 2026, if (alertas) 1 else 0, usuarioId, 3))
                }
            }
        }
        item {
            ExpandableCategoryItem(nombreCategoria = "Alimentación", icono = Icons.Default.ShoppingCart) {
                CategoryBudgetCard("Alimentación", gastadoAlimentacion, montoPresupuestoAlimentacion, true) { monto, alertas ->
                    if (usuarioId.isNotBlank()) presupuestoViewModel.insertarPresupuesto(Presupuesto(presupuestoAlimentacion?.idPresupuesto ?: 0, monto, 5, 2026, if (alertas) 1 else 0, usuarioId, 4))
                }
            }
        }
        item {
            ExpandableCategoryItem(nombreCategoria = "Entretenimiento", icono = Icons.Default.Movie) {
                CategoryBudgetCard("Entretenimiento", gastadoEntretenimiento, montoPresupuestoEntretenimiento, false) { monto, alertas ->
                    if (usuarioId.isNotBlank()) presupuestoViewModel.insertarPresupuesto(Presupuesto(presupuestoEntretenimiento?.idPresupuesto ?: 0, monto, 5, 2026, if (alertas) 1 else 0, usuarioId, 2))
                }
            }
        }
        item {
            ExpandableCategoryItem(nombreCategoria = "Salud", icono = Icons.Default.Favorite) {
                CategoryBudgetCard("Salud", gastadoSalud, montoPresupuestoSalud, true) { monto, alertas ->
                    if (usuarioId.isNotBlank()) presupuestoViewModel.insertarPresupuesto(Presupuesto(presupuestoSalud?.idPresupuesto ?: 0, monto, 5, 2026, if (alertas) 1 else 0, usuarioId, 5))
                }
            }
        }
        item {
            ExpandableCategoryItem(nombreCategoria = "Otros", icono = Icons.Default.MoreHoriz) {
                CategoryBudgetCard("Otros", gastadoOtros, montoPresupuestoOtros, true) { monto, alertas ->
                    if (usuarioId.isNotBlank()) presupuestoViewModel.insertarPresupuesto(Presupuesto(presupuestoOtros?.idPresupuesto ?: 0, monto, 5, 2026, if (alertas) 1 else 0, usuarioId, 6))
                }
            }
        }
    }
}

@Composable
private fun DisponibleParaDistribuirBanner(disponible: Double, formateado: String) {
    val esPositivo = disponible >= 0
    val colorFondo = if (esPositivo) TealDark else Color(0xFFB00020)
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).clip(RoundedCornerShape(12.dp)).background(colorFondo).padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(if (esPositivo) Icons.Default.AccountBalance else Icons.Default.Warning, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Text(if (esPositivo) "Disponible para distribuir" else "Presupuesto excedido", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }
        Text(if (esPositivo) formateado else "- $formateado", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
    }
}

@Composable
fun PresupuestosToggle(isFijosSelected: Boolean, onFijosClick: () -> Unit, onCategoriasClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).background(Color(0xFFF3F3F3), RoundedCornerShape(12.dp)).padding(4.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.weight(1f).clip(RoundedCornerShape(10.dp)).background(if (isFijosSelected) Color.White else Color.Transparent).clickable { onFijosClick() }.padding(vertical = 10.dp), contentAlignment = Alignment.Center) {
            Text("Fijos", color = if (isFijosSelected) TealDark else Color.Gray, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
        Box(modifier = Modifier.weight(1f).clip(RoundedCornerShape(10.dp)).background(if (!isFijosSelected) Color.White else Color.Transparent).clickable { onCategoriasClick() }.padding(vertical = 10.dp), contentAlignment = Alignment.Center) {
            Text("Por Categoría", color = if (!isFijosSelected) TealDark else Color.Gray, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}




















@Composable
fun PresupuestoSummaryCard(
    ingresosFijos: Double,
    gastosFijos: Double,
    presupuestoUsado: Double,
    presupuestoTotal: Double
) {
    val disponible = ingresosFijos - gastosFijos
    val progreso = if (presupuestoTotal > 0) (presupuestoUsado / presupuestoTotal).toFloat() else 0f

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("Ingresos Fijos", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                    Text("+$${ingresosFijos}", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Gastos Fijos", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                    Text("-$${gastosFijos}", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                }
            }

            Divider(color = Color.White.copy(alpha = 0.3f), thickness = 1.dp, modifier = Modifier.padding(vertical = 12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Disponible", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("$$disponible", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 24.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Presupuesto Total", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                Text("Usado $$presupuestoUsado / $$presupuestoTotal", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { progreso.coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = MagentaPink,
                trackColor = Color.White.copy(alpha = 0.3f)
            )
        }
    }
}















@Composable
fun ExpandableFixedTransactionFAB(
    onAddIngresoClick: () -> Unit,
    onAddGastoClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(horizontalAlignment = Alignment.End) {
        AnimatedVisibility(visible = expanded) {
            Column(horizontalAlignment = Alignment.End, modifier = Modifier.padding(bottom = 16.dp)) {
                ExtendedFloatingActionButton(
                    text = { Text("Añadir Gasto Fijo", color = Color.White, fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.Add, contentDescription = null, tint = Color.White) },
                    onClick = { expanded = false; onAddGastoClick() },
                    containerColor = MagentaPink,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                ExtendedFloatingActionButton(
                    text = { Text("Añadir Ingreso Fijo", color = Color.White, fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.Add, contentDescription = null, tint = Color.White) },
                    onClick = { expanded = false; onAddIngresoClick() },
                    containerColor = TealDark
                )
            }
        }

        FloatingActionButton(
            onClick = { expanded = !expanded },
            containerColor = TealDark,
            contentColor = Color.White,
            shape = CircleShape
        ) {
            Icon(
                imageVector = if (expanded) Icons.Default.Close else Icons.Default.Add,
                contentDescription = "Expandir"
            )
        }
    }
}








@Composable
fun CategoryBudgetCard(
    nombreCategoria: String,
    gastado: Double,
    presupuestoAsignado: Double,
    alertasActivadas: Boolean,
    onSaveConfig: (nuevoPresupuesto: Double, nuevasAlertas: Boolean) -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var inputPresupuesto by remember { mutableStateOf(presupuestoAsignado.toString()) }
    var inputAlertas by remember { mutableStateOf(alertasActivadas) }

    val progreso = if (presupuestoAsignado > 0) (gastado / presupuestoAsignado).toFloat() else 0f
    val isWarning = progreso >= 0.9f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(nombreCategoria.uppercase(), fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = DarkGrayText, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = inputPresupuesto,
                onValueChange = { inputPresupuesto = it },
                label = { Text("Presupuesto Asignado") },
                leadingIcon = { Text("$", color = DarkGrayText) },
                enabled = isEditing,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TealDark,
                    unfocusedBorderColor = LightBlueGray,
                    disabledBorderColor = Color.Transparent,
                    disabledTextColor = DarkGrayText
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Activar Alertas", fontWeight = FontWeight.Bold, color = DarkGrayText, fontSize = 14.sp)
                Switch(
                    checked = inputAlertas,
                    onCheckedChange = { inputAlertas = it },
                    enabled = isEditing,
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = TealLight)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text("${(progreso * 100).toInt()}%", fontWeight = FontWeight.ExtraBold, fontSize = 24.sp, color = if (isWarning) ErrorRed else TealDark)
                Text("Gastado: $$gastado / $$presupuestoAsignado", color = LightBlueGray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                LinearProgressIndicator(
                    progress = { progreso.coerceIn(0f, 1f) },
                    modifier = Modifier.weight(1f).height(8.dp).clip(RoundedCornerShape(4.dp)),
                    color = if (isWarning) ErrorRed else TealDark,
                    trackColor = BackgroundLight
                )
                if (isWarning) {
                    Icon(Icons.Default.WarningAmber, contentDescription = "Advertencia", tint = ErrorRed, modifier = Modifier.padding(start = 8.dp).size(20.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isEditing) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = {
                            isEditing = false
                            inputPresupuesto = presupuestoAsignado.toString()
                            inputAlertas = alertasActivadas
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = DarkGrayText)
                    ) { Text("Cancelar", fontWeight = FontWeight.Bold) }

                    Button(
                        onClick = {
                            isEditing = false
                            val nuevoMonto = inputPresupuesto.toDoubleOrNull() ?: presupuestoAsignado
                            onSaveConfig(nuevoMonto, inputAlertas)
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = MagentaPink)
                    ) { Text("Guardar", fontWeight = FontWeight.Bold) }
                }
            } else {
                Button(
                    onClick = { isEditing = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = TealDark),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Configurar", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}