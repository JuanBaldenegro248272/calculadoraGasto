package LosPrimos.Durango.calculadoragastos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import LosPrimos.Durango.calculadoragastos.data.entities.Presupuesto
import LosPrimos.Durango.calculadoragastos.ui.components.*
import LosPrimos.Durango.calculadoragastos.ui.theme.*
import LosPrimos.Durango.calculadoragastos.viewModel.GastoViewModel
import LosPrimos.Durango.calculadoragastos.viewModel.PresupuestoViewModel

@Composable
fun PresupuestosScreen(
    onNavigate: (String) -> Unit,
    presupuestoViewModel: PresupuestoViewModel,
    gastoViewModel: GastoViewModel
) {
    var isCategoriaSelected by remember { mutableStateOf(true) }
    var showAddGastoDialog by remember { mutableStateOf(false) }
    var showAddIngresoDialog by remember { mutableStateOf(false) }
    val usuarioActualId by presupuestoViewModel.usuarioActualId.collectAsState()
    val usuarioId = usuarioActualId ?: 0

    val presupuestos by presupuestoViewModel
        .obtenerPresupuesto(usuarioId)
        .collectAsState(initial = emptyList())

    val gastos by gastoViewModel
        .obtenerGastosPorUsuario(usuarioId)
        .collectAsState(initial = emptyList())

    val ingresosFijosTotales = 0.0
    val gastosFijosTotales = 0.0
    val disponibleFijo = ingresosFijosTotales - gastosFijosTotales
    val presupuestoAsignadoTotal = presupuestos.sumOf { it.monto }
    val presupuestoGastado = gastos.sumOf { gasto ->
        if (presupuestos.any { presupuesto -> presupuesto.idCategoria == gasto.idCategoria }) {
            gasto.monto
        } else {
            0.0
        }
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


    Scaffold(
        bottomBar = {
            SpentBottomNavigation(
                currentRoute = "presupuestos",
                onNavigate = { onNavigate(it) }
            )
        },
        floatingActionButton = {
            ExpandableFixedTransactionFAB(
                onAddIngresoClick = { showAddIngresoDialog = true },
                onAddGastoClick = { showAddGastoDialog = true }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MainGradient)
                .padding(paddingValues)
        ) {



            if (showAddGastoDialog) {
                AddFixedTransactionDialog(
                    isGasto = true,
                    onDismiss = { showAddGastoDialog = false },
                    onConfirm = { nombre, monto, dia ->
                        println("Nuevo Gasto Fijo: $nombre, $monto, día $dia")
                        showAddGastoDialog = false
                    }
                )
            }

            if (showAddIngresoDialog) {
                AddFixedTransactionDialog(
                    isGasto = false,
                    onDismiss = { showAddIngresoDialog = false },
                    onConfirm = { nombre, monto, dia ->
                        println("Nuevo Ingreso Fijo: $nombre, $monto, día $dia")
                        showAddIngresoDialog = false
                    }
                )
            }



            Column(modifier = Modifier.fillMaxSize()) {

                PresupuestoTopBar()

                PresupuestoSummaryCard(
                    ingresosFijos = ingresosFijosTotales,
                    gastosFijos = gastosFijosTotales,
                    presupuestoUsado = presupuestoGastado,
                    presupuestoTotal = presupuestoAsignadoTotal
                )

                Spacer(modifier = Modifier.height(24.dp))

                BottomRoundedSurface {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(top = 24.dp, bottom = 100.dp)
                    ) {
                        item {
                            PresupuestoToggle(
                                isCategoriaSelected = isCategoriaSelected,
                                onCategoriaClick = { isCategoriaSelected = true },
                                onFijosClick = { isCategoriaSelected = false }
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                        }

                        if (isCategoriaSelected) {
                            item {
                                CategoryBudgetCard(
                                    nombreCategoria = "Vivienda",
                                    gastado = gastadoVivienda,
                                    presupuestoAsignado = montoPresupuestoVivienda,
                                    alertasActivadas = true,
                                    onSaveConfig = { monto, alertas ->
                                        if (usuarioId != 0) {
                                            val nuevoPresupuesto = Presupuesto(
                                                idPresupuesto = presupuestoVivienda?.idPresupuesto ?: 0,
                                                monto = monto,
                                                mes = 5,
                                                anio = 2026,
                                                porcentaje = if (alertas) 1 else 0,
                                                idUsuario = usuarioId,
                                                idCategoria = 1
                                            )

                                            presupuestoViewModel.insertarPresupuesto(nuevoPresupuesto)
                                        }
                                    }
                                )
                            }
                            item {
                                CategoryBudgetCard(
                                    nombreCategoria = "Transporte",
                                    gastado = gastadoTransporte,
                                    presupuestoAsignado = montoPresupuestoTransporte,
                                    alertasActivadas = true,
                                    onSaveConfig = { monto, alertas ->
                                        if (usuarioId != 0) {
                                            val nuevoPresupuesto = Presupuesto(
                                                idPresupuesto = presupuestoTransporte?.idPresupuesto ?: 0,
                                                monto = monto,
                                                mes = 5,
                                                anio = 2026,
                                                porcentaje = if (alertas) 1 else 0,
                                                idUsuario = usuarioId,
                                                idCategoria = 3
                                            )

                                            presupuestoViewModel.insertarPresupuesto(nuevoPresupuesto)
                                        }
                                    }
                                )
                            }
                            item {
                                CategoryBudgetCard(
                                    nombreCategoria = "Alimentación",
                                    gastado = gastadoAlimentacion,
                                    presupuestoAsignado = montoPresupuestoAlimentacion,
                                    alertasActivadas = true,
                                    onSaveConfig = { monto, alertas ->
                                        if (usuarioId != 0) {
                                            val nuevoPresupuesto = Presupuesto(
                                                idPresupuesto = presupuestoAlimentacion?.idPresupuesto ?: 0,
                                                monto = monto,
                                                mes = 5,
                                                anio = 2026,
                                                porcentaje = if (alertas) 1 else 0,
                                                idUsuario = usuarioId,
                                                idCategoria = 4
                                            )

                                            presupuestoViewModel.insertarPresupuesto(nuevoPresupuesto)
                                        }
                                    }
                                )
                            }
                            item {
                                CategoryBudgetCard(
                                    nombreCategoria = "Entretenimiento",
                                    gastado = gastadoEntretenimiento,
                                    presupuestoAsignado = montoPresupuestoEntretenimiento,
                                    alertasActivadas = false,
                                    onSaveConfig = { monto, alertas ->
                                        if (usuarioId != 0) {
                                            val nuevoPresupuesto = Presupuesto(
                                                idPresupuesto = presupuestoEntretenimiento?.idPresupuesto ?: 0,
                                                monto = monto,
                                                mes = 5,
                                                anio = 2026,
                                                porcentaje = if (alertas) 1 else 0,
                                                idUsuario = usuarioId,
                                                idCategoria = 2
                                            )

                                            presupuestoViewModel.insertarPresupuesto(nuevoPresupuesto)
                                        }
                                    }
                                )
                            }
                            item {
                                CategoryBudgetCard(
                                    nombreCategoria = "Salud",
                                    gastado = gastadoSalud,
                                    presupuestoAsignado = montoPresupuestoSalud,
                                    alertasActivadas = true,
                                    onSaveConfig = { monto, alertas ->
                                        if (usuarioId != 0) {
                                            val nuevoPresupuesto = Presupuesto(
                                                idPresupuesto = presupuestoSalud?.idPresupuesto ?: 0,
                                                monto = monto,
                                                mes = 5,
                                                anio = 2026,
                                                porcentaje = if (alertas) 1 else 0,
                                                idUsuario = usuarioId,
                                                idCategoria = 5
                                            )

                                            presupuestoViewModel.insertarPresupuesto(nuevoPresupuesto)
                                        }
                                    }
                                )
                            }
                            item {
                                CategoryBudgetCard(
                                    nombreCategoria = "Otros",
                                    gastado = gastadoOtros,
                                    presupuestoAsignado = montoPresupuestoOtros,
                                    alertasActivadas = true,
                                    onSaveConfig = { monto, alertas ->
                                        if (usuarioId != 0) {
                                            val nuevoPresupuesto = Presupuesto(
                                                idPresupuesto = presupuestoOtros?.idPresupuesto ?: 0,
                                                monto = monto,
                                                mes = 5,
                                                anio = 2026,
                                                porcentaje = if (alertas) 1 else 0,
                                                idUsuario = usuarioId,
                                                idCategoria = 6
                                            )

                                            presupuestoViewModel.insertarPresupuesto(nuevoPresupuesto)
                                        }
                                    }
                                )
                            }
                        } else {
                            item {
                                CategoryGroup(
                                    nombreCategoria = "Ingresos Fijos ($+${ingresosFijosTotales})",
                                    iconoCategoria = Icons.Default.ArrowUpward
                                ) {
                                    TransactionItem(titulo = "Sueldo Quincenal", fecha = "Días 15 y 30", monto = 15000.0, isGasto = false)
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                CategoryGroup(
                                    nombreCategoria = "Gastos Fijos ($-${gastosFijosTotales})",
                                    iconoCategoria = Icons.Default.ArrowDownward
                                ) {
                                    TransactionItem(titulo = "Renta Departamento", fecha = "Día 1 de cada mes", monto = 3500.0, isGasto = true)
                                    TransactionItem(titulo = "Recibo de Internet", fecha = "Día 12 de cada mes", monto = 500.0, isGasto = true)
                                    TransactionItem(titulo = "Suscripción Spotify", fecha = "Día 5 de cada mes", monto = 500.0, isGasto = true)
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 24.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = BackgroundLight)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(20.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "Disponible para distribuir",
                                            color = LightBlueGray,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        )
                                        Text(
                                            text = "$${disponibleFijo}",
                                            color = TealDark,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 28.sp,
                                            textAlign = TextAlign.Center
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
