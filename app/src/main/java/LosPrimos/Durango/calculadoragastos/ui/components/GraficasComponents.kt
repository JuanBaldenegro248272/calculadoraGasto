package LosPrimos.Durango.calculadoragastos.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import LosPrimos.Durango.calculadoragastos.ui.theme.*
import androidx.compose.ui.text.style.TextAlign
import kotlin.math.max

data class CategoriaGraficaData(
    val nombre: String,
    val gastado: Float,
    val esperado: Float,
    val color: Color
)

@Composable
fun GraficasTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Gráfica de Gastos",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )
    }
}

@Composable
fun GraficaPresupuestoCard(
    montoTotal: Double,
    icono: ImageVector = Icons.Default.AccountBalanceWallet,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.2f)
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icono, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Presupuesto Gastado", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("$$montoTotal", fontWeight = FontWeight.ExtraBold, fontSize = 28.sp, color = Color.White)
        }
    }
}

@Composable
fun GraficasFiltrosSection(
    categoriaSeleccionada: String,
    onCategoriaChange: (String) -> Unit,
    mesSeleccionado: String,
    onMesChange: (String) -> Unit
) {
    val categorias = listOf("Todas", "Transporte", "Salud", "Entretenimiento", "Alimentacion")
    val meses = listOf("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .background(BackgroundLight, RoundedCornerShape(16.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        GraficaDropdown("Categoría", categoriaSeleccionada, categorias, onCategoriaChange)
        GraficaDropdown("Fecha", mesSeleccionado, meses, onMesChange)
    }
}

@Composable
private fun GraficaDropdown(label: String, selected: String, items: List<String>, onSelect: (String) -> Unit) {
    var expandido by remember { mutableStateOf(false) }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontSize = 12.sp, color = LightBlueGray, fontWeight = FontWeight.Bold)
        Box {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { expandido = true }.padding(top = 4.dp)
            ) {
                Text(text = selected, color = TealDark, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = TealDark)
            }
            DropdownMenu(expanded = expandido, onDismissRequest = { expandido = false }, modifier = Modifier.background(Color.White)) {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item, color = DarkGrayText) },
                        onClick = { onSelect(item); expandido = false }
                    )
                }
            }
        }
    }
}

@Composable
fun GraficaPastelNativa(datos: List<CategoriaGraficaData>, presupuestoTotal: Float) {
    val totalGastado = datos.sumOf { it.gastado.toDouble() }.toFloat()
    val restante = max(0f, presupuestoTotal - totalGastado)

    val rebanadas = datos.map { it.gastado to it.color } + listOf(restante to LightBlueGray.copy(alpha = 0.3f))

    Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(160.dp)) {
            var startAngle = -90f
            val total = presupuestoTotal

            rebanadas.forEach { (valor, color) ->
                val sweepAngle = (valor / total) * 360f
                if (sweepAngle > 0) {
                    drawArc(
                        color = color,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = true,
                        size = Size(size.width, size.height)
                    )
                    startAngle += sweepAngle
                }
            }

        }


    }
}

@Composable
fun GraficaBarrasAgrupadas(datos: List<CategoriaGraficaData>) {
    val colorEsperadoGlobal = LightBlueGray.copy(alpha = 0.5f)
    val maxValor = datos.maxOfOrNull { max(it.gastado, it.esperado) } ?: 1f

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        val width = size.width
        val height = size.height
        val groupWidth = width / datos.size
        val barWidth = groupWidth * 0.3f
        val cornerRadius = CornerRadius(4.dp.toPx())

        datos.forEachIndexed { index, data ->
            val startX = index * groupWidth
            val centerX = startX + (groupWidth / 2f)

            // Alturas max
            val alturaGastado = (data.gastado / maxValor) * height
            val alturaEsperado = (data.esperado / maxValor) * height

            // barra esperado
            drawRoundRect(
                color = colorEsperadoGlobal,
                topLeft = Offset(centerX - barWidth - 4.dp.toPx(), height - alturaEsperado),
                size = Size(barWidth, alturaEsperado),
                cornerRadius = cornerRadius
            )

            // barra gastado
            drawRoundRect(
                color = data.color,
                topLeft = Offset(centerX + 4.dp.toPx(), height - alturaGastado),
                size = Size(barWidth, alturaGastado),
                cornerRadius = cornerRadius
            )
        }
    }
}

@Composable
fun TablaResumenGastos(datos: List<CategoriaGraficaData>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Categoría", modifier = Modifier.weight(1.2f), fontSize = 12.sp, color = LightBlueGray, fontWeight = FontWeight.Bold)
            Text("Gastado", modifier = Modifier.weight(1f), fontSize = 12.sp, color = LightBlueGray, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Text("Esperado", modifier = Modifier.weight(1f), fontSize = 12.sp, color = LightBlueGray, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        }
        Divider(color = LightBlueGray.copy(alpha = 0.2f), thickness = 1.dp)

        datos.forEach { data ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1.2f)) {
                    Box(modifier = Modifier.size(10.dp).background(data.color, CircleShape))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(data.nombre, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DarkGrayText)
                }

                val colorGastado = if (data.gastado > data.esperado) ErrorRed else DarkGrayText
                Text(
                    text = "$${data.gastado}",
                    modifier = Modifier.weight(1f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = colorGastado,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "$${data.esperado}",
                    modifier = Modifier.weight(1f),
                    fontSize = 14.sp,
                    color = LightBlueGray,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}