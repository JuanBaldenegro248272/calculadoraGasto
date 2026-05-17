package LosPrimos.Durango.calculadoragastos.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import LosPrimos.Durango.calculadoragastos.ui.theme.*
import LosPrimos.Durango.calculadoragastos.ui.components.*
import LosPrimos.Durango.calculadoragastos.viewModel.CategoriaViewModel
import LosPrimos.Durango.calculadoragastos.viewModel.GastoViewModel
import LosPrimos.Durango.calculadoragastos.viewModel.IngresoViewModel
import LosPrimos.Durango.calculadoragastos.viewModel.PresupuestoViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GraficasScreen(
    onNavigate: (String) -> Unit,
    gastoViewModel: GastoViewModel,
    ingresoViewModel: IngresoViewModel,
    presupuestoViewModel: PresupuestoViewModel,
    categoriaViewModel: CategoriaViewModel
) {
    val usuarioActualId by ingresoViewModel.usuarioActualId.collectAsState()
    val usuarioId = usuarioActualId ?: ""

    val gastos by gastoViewModel.obtenerGastosPorUsuario(usuarioId)
        .collectAsState(initial = emptyList())
    val presupuestos by presupuestoViewModel.obtenerPresupuesto(usuarioId)
        .collectAsState(initial = emptyList())
    val categorias by categoriaViewModel.obtenerCategorias()
        .collectAsState(initial = emptyList())

    val mesesMap = mapOf(
        "Enero" to 1, "Febrero" to 2, "Marzo" to 3, "Abril" to 4,
        "Mayo" to 5, "Junio" to 6, "Julio" to 7, "Agosto" to 8,
        "Septiembre" to 9, "Octubre" to 10, "Noviembre" to 11, "Diciembre" to 12
    )
    val mesesMapReverso = mesesMap.entries.associate { (k, v) -> v to k }
    val mesActualTexto = remember { mesesMapReverso[LocalDate.now().monthValue] ?: "Enero" }

    var categoriaSeleccionada by remember { mutableStateOf("Todas") }
    var mesSeleccionado by remember { mutableStateOf(mesActualTexto) }

    val nombresCategorias = listOf("Todas") + categorias.map { it.nombre }

    val gastosFiltrados = remember(gastos, mesSeleccionado, categorias) {
        gastos.filter { gasto ->
            if (gasto.esFijo) return@filter false
            val fecha = Instant.ofEpochMilli(gasto.fecha)
                .atZone(ZoneId.systemDefault()).toLocalDate()
            fecha.monthValue == (mesesMap[mesSeleccionado] ?: LocalDate.now().monthValue)
        }
    }

    val datosGrafica: List<CategoriaGraficaData> = remember(
        gastosFiltrados, presupuestos, categorias, categoriaSeleccionada
    ) {
        val coloresCategorias = mapOf(
            1 to ViviendaColor,
            2 to EntretenimientoColor,
            3 to TealDark,
            4 to YellowGraph,
            5 to MagentaPink,
            6 to GreenGraph
        )

        categorias
            .filter { cat ->
                categoriaSeleccionada == "Todas" || cat.nombre == categoriaSeleccionada
            }
            .mapNotNull { cat ->
                val gastado = gastosFiltrados
                    .filter { it.idCategoria == cat.idCategoria }
                    .sumOf { it.monto }
                    .toFloat()
                val esperado = presupuestos
                    .find { it.idCategoria == cat.idCategoria }
                    ?.monto?.toFloat() ?: 0f
                
                if (gastado == 0f && esperado == 0f) return@mapNotNull null

                CategoriaGraficaData(
                    nombre = cat.nombre,
                    gastado = gastado,
                    esperado = esperado,
                    color = coloresCategorias[cat.idCategoria] ?: GreenGraph
                )
            }
    }

    val presupuestoTotal = presupuestos.sumOf { it.monto }.toFloat()
    val totalGastado = gastosFiltrados.sumOf { it.monto }.toFloat()

    Scaffold(
        bottomBar = {
            SpentBottomNavigation(
                currentRoute = "graficas",
                onNavigate = { onNavigate(it) }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MainGradient)
                .padding(paddingValues)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {

                GraficasTopBar()

                GraficaPresupuestoCard(
                    montoGastado = totalGastado.toDouble(),
                    montoTotal = presupuestoTotal.toDouble()
                )

                Spacer(modifier = Modifier.height(24.dp))

                BottomRoundedSurface {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(top = 8.dp, bottom = 80.dp)
                    ) {
                        item {
                            GraficasFiltrosSection(
                                categoriaSeleccionada = categoriaSeleccionada,
                                onCategoriaChange = { categoriaSeleccionada = it },
                                mesSeleccionado = mesSeleccionado,
                                onMesChange = { mesSeleccionado = it },
                                categorias = nombresCategorias
                            )
                        }

                        if (datosGrafica.isEmpty()) {
                            item {
                                Spacer(modifier = Modifier.height(40.dp))
                                Text(
                                    text = "Sin datos para el período seleccionado",
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    color = LightBlueGray,
                                    fontSize = 14.sp
                                )
                            }
                        } else {

                            item {
                                Spacer(modifier = Modifier.height(24.dp))
                                TablaResumenGastos(datos = datosGrafica)
                            }

                            item {
                                Spacer(modifier = Modifier.height(32.dp))
                                Text(
                                    text = "Gráfica de pastel",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BackgroundDark,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                GraficaPastelNativa(
                                    datos = datosGrafica,
                                    presupuestoTotal = presupuestoTotal
                                )
                            }

                            item {
                                Spacer(modifier = Modifier.height(32.dp))
                                Text(
                                    text = "Gastado vs Presupuestado",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BackgroundDark,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                GraficaBarrasAgrupadas(datos = datosGrafica)
                            }

                            item {
                                Spacer(modifier = Modifier.height(32.dp))
                                Text(
                                    text = "Progreso por categoría",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BackgroundDark,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                ProgresoCategorias(datos = datosGrafica)
                            }
                        }
                    }
                }
            }
        }
    }
}