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
import LosPrimos.Durango.calculadoragastos.ui.components.*
import LosPrimos.Durango.calculadoragastos.ui.theme.*

@Composable
fun PresupuestosScreen(
    onNavigate: (String) -> Unit
) {
    var isCategoriaSelected by remember { mutableStateOf(true) }
    var showAddGastoDialog by remember { mutableStateOf(false) }
    var showAddIngresoDialog by remember { mutableStateOf(false) }

    val ingresosFijosTotales = 15000.0
    val gastosFijosTotales = 4500.0
    val disponibleFijo = ingresosFijosTotales - gastosFijosTotales
    val presupuestoAsignadoTotal = 10000.0
    val presupuestoGastado = 6500.0

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
                                    nombreCategoria = "Transporte",
                                    gastado = 800.0,
                                    presupuestoAsignado = 1500.0,
                                    alertasActivadas = true,
                                    onSaveConfig = { monto, alertas -> }
                                )
                            }
                            item {
                                CategoryBudgetCard(
                                    nombreCategoria = "Alimentación",
                                    gastado = 4500.0,
                                    presupuestoAsignado = 5000.0,
                                    alertasActivadas = true,
                                    onSaveConfig = { monto, alertas -> }
                                )
                            }
                            item {
                                CategoryBudgetCard(
                                    nombreCategoria = "Entretenimiento",
                                    gastado = 1200.0,
                                    presupuestoAsignado = 1000.0,
                                    alertasActivadas = false,
                                    onSaveConfig = { monto, alertas ->  }
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