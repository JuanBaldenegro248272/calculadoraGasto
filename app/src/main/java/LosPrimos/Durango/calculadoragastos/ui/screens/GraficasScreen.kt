package LosPrimos.Durango.calculadoragastos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import LosPrimos.Durango.calculadoragastos.ui.theme.*
import LosPrimos.Durango.calculadoragastos.ui.components.*
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun GraficasScreen(
    onNavigate: (String) -> Unit
) {
    var categoriaSeleccionada by remember { mutableStateOf("Todas") }
    var mesSeleccionado by remember { mutableStateOf("Abril") }

    //mock
    val presupuestoTotal = 1500f
    val datosGrafica = listOf(
        CategoriaGraficaData("Transporte", 500f, 1000f, TealDark),
        CategoriaGraficaData("Salud", 250f, 200f, MagentaPink),
        CategoriaGraficaData("Alimentacion", 250f, 300f, YellowGraph),
        CategoriaGraficaData("Vivienda", 400f, 400f, GreenGraph)
    )

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
                                onMesChange = { mesSeleccionado = it }
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(32.dp))
                            TablaResumenGastos(
                                datos = datosGrafica
                            )
                        }


                        item {
                            Spacer(modifier = Modifier.height(24.dp))

                            Text(
                                text = "Grafica de pastel",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = BackgroundDark,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(24.dp))
                        }

                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            GraficaPastelNativa(
                                datos = datosGrafica,
                                presupuestoTotal = presupuestoTotal
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(24.dp))

                            Text(
                                text = "Grafica de barras: Gastado VS Esperado",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = BackgroundDark,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(24.dp))
                        }


                        item {
                            Spacer(modifier = Modifier.height(32.dp))
                            GraficaBarrasAgrupadas(
                                datos = datosGrafica
                            )
                        }

                    }
                }
            }
        }
    }
}