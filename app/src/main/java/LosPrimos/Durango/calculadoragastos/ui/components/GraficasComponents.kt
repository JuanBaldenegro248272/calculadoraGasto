package LosPrimos.Durango.calculadoragastos.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import LosPrimos.Durango.calculadoragastos.ui.theme.*
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
            text = "Gráficas de Gastos",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )
    }
}
@Composable
fun GraficaPresupuestoCard(
    montoGastado: Double,
    montoTotal: Double,
    icono: ImageVector = Icons.Default.AccountBalanceWallet,
    modifier: Modifier = Modifier
) {
    val porcentaje = if (montoTotal > 0) (montoGastado / montoTotal).toFloat() else 0f
    val colorBarra = when {
        porcentaje >= 1f -> ErrorRed
        porcentaje >= 0.8f -> YellowGraph
        else -> GreenGraph
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.2f)),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icono, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Presupuesto del mes",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    "$${"%.2f".format(montoGastado)}",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 28.sp,
                    color = Color.White
                )
                Text(
                    "/ $${"%.2f".format(montoTotal)}",
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.75f)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            LinearProgressIndicator(
                progress = porcentaje.coerceIn(0f, 1f),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = colorBarra,
                trackColor = Color.White.copy(alpha = 0.3f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${"%.0f".format(porcentaje * 100)}% utilizado",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun GraficasFiltrosSection(
    categoriaSeleccionada: String,
    onCategoriaChange: (String) -> Unit,
    mesSeleccionado: String,
    onMesChange: (String) -> Unit,
    categorias: List<String> = listOf("Todas", "Transporte", "Salud", "Entretenimiento", "Alimentacion", "Vivienda", "Otros")
) {
    val meses = listOf(
        "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
        "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .background(BackgroundLight, RoundedCornerShape(16.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        GraficaDropdown("Categoría", categoriaSeleccionada, categorias, onCategoriaChange)
        GraficaDropdown("Mes", mesSeleccionado, meses, onMesChange)
    }
}

@Composable
private fun GraficaDropdown(
    label: String,
    selected: String,
    items: List<String>,
    onSelect: (String) -> Unit
) {
    var expandido by remember { mutableStateOf(false) }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontSize = 12.sp, color = LightBlueGray, fontWeight = FontWeight.Bold)
        Box {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable { expandido = true }
                    .padding(top = 4.dp)
            ) {
                Text(selected, color = TealDark, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = TealDark)
            }
            DropdownMenu(
                expanded = expandido,
                onDismissRequest = { expandido = false },
                modifier = Modifier.background(Color.White)
            ) {
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
fun TablaResumenGastos(datos: List<CategoriaGraficaData>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        // Encabezado
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Categoría", modifier = Modifier.weight(1.2f), fontSize = 12.sp, color = LightBlueGray, fontWeight = FontWeight.Bold)
            Text("Gastado", modifier = Modifier.weight(1f), fontSize = 12.sp, color = LightBlueGray, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Text("Presupuesto", modifier = Modifier.weight(1f), fontSize = 12.sp, color = LightBlueGray, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        }
        HorizontalDivider(color = LightBlueGray.copy(alpha = 0.2f), thickness = 1.dp)

        datos.forEach { data ->
            val excedido = data.gastado > data.esperado && data.esperado > 0
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1.2f)) {
                    Box(modifier = Modifier.size(10.dp).background(data.color, CircleShape))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(data.nombre, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DarkGrayText, maxLines = 1)
                }
                Text(
                    text = "$${"%.0f".format(data.gastado)}",
                    modifier = Modifier.weight(1f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (excedido) ErrorRed else DarkGrayText,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = if (data.esperado > 0) "$${"%.0f".format(data.esperado)}" else "—",
                    modifier = Modifier.weight(1f),
                    fontSize = 14.sp,
                    color = LightBlueGray,
                    textAlign = TextAlign.Center
                )
            }
            HorizontalDivider(color = LightBlueGray.copy(alpha = 0.1f), thickness = 0.5.dp)
        }
    }
}

@Composable
fun GraficaPastelNativa(datos: List<CategoriaGraficaData>, presupuestoTotal: Float) {
    val totalGastado = datos.sumOf { it.gastado.toDouble() }.toFloat()
    val restante = max(0f, presupuestoTotal - totalGastado)

    val rebanadas: List<Pair<Float, Color>> = datos.map { it.gastado to it.color } +
            if (restante > 0) listOf(restante to LightBlueGray.copy(alpha = 0.3f)) else emptyList()

    val base = if (presupuestoTotal > 0) presupuestoTotal else totalGastado.coerceAtLeast(1f)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(180.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(180.dp)) {
                var startAngle = -90f
                rebanadas.forEach { (valor, color) ->
                    val sweepAngle = (valor / base) * 360f
                    if (sweepAngle > 0.5f) {
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
                drawCircle(color = Color.White, radius = size.minDimension * 0.32f)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${"%.0f".format(if (base > 0) (totalGastado / base) * 100 else 0f)}%",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = BackgroundDark
                )
                Text(text = "gastado", fontSize = 11.sp, color = LightBlueGray)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            datos.forEach { data ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(modifier = Modifier.size(12.dp).background(data.color, CircleShape))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(data.nombre, fontSize = 13.sp, color = DarkGrayText, modifier = Modifier.weight(1f))
                    Text(
                        "$${"%.0f".format(data.gastado)}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkGrayText
                    )
                }
            }
            if (restante > 0) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(modifier = Modifier.size(12.dp).background(LightBlueGray.copy(alpha = 0.4f), CircleShape))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Disponible", fontSize = 13.sp, color = LightBlueGray, modifier = Modifier.weight(1f))
                    Text("$${"%.0f".format(restante)}", fontSize = 13.sp, color = LightBlueGray)
                }
            }
        }
    }
}
@Composable
fun ProgresoCategorias(datos: List<CategoriaGraficaData>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        datos.forEach { data ->
            val porcentaje = if (data.esperado > 0) (data.gastado / data.esperado).coerceIn(0f, 1f) else 0f
            val animatedProgress by animateFloatAsState(
                targetValue = porcentaje,
                animationSpec = tween(durationMillis = 800),
                label = "progreso_${data.nombre}"
            )
            val excedido = data.gastado > data.esperado && data.esperado > 0

            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(10.dp).background(data.color, CircleShape))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(data.nombre, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DarkGrayText)
                    }
                    Text(
                        text = if (data.esperado > 0)
                            "${"%.0f".format(porcentaje * 100)}%"
                        else
                            "Sin límite",
                        fontSize = 12.sp,
                        color = if (excedido) ErrorRed else LightBlueGray,
                        fontWeight = if (excedido) FontWeight.Bold else FontWeight.Normal
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(LightBlueGray.copy(alpha = 0.2f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(animatedProgress)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(5.dp))
                            .background(if (excedido) ErrorRed else data.color)
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "$${"%.0f".format(data.gastado)} gastado",
                        fontSize = 11.sp,
                        color = if (excedido) ErrorRed else DarkGrayText
                    )
                    if (data.esperado > 0) {
                        Text(
                            "de $${"%.0f".format(data.esperado)}",
                            fontSize = 11.sp,
                            color = LightBlueGray
                        )
                    }
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
        val groupWidth = width / datos.size.coerceAtLeast(1)
        val barWidth = groupWidth * 0.3f
        val cornerRadius = CornerRadius(4.dp.toPx())

        datos.forEachIndexed { index, data ->
            val startX = index * groupWidth
            val centerX = startX + (groupWidth / 2f)

            val alturaGastado = (data.gastado / maxValor) * height
            val alturaEsperado = (data.esperado / maxValor) * height

            drawRoundRect(
                color = colorEsperadoGlobal,
                topLeft = Offset(centerX - barWidth - 4.dp.toPx(), height - alturaEsperado),
                size = Size(barWidth, alturaEsperado),
                cornerRadius = cornerRadius
            )

            drawRoundRect(
                color = data.color,
                topLeft = Offset(centerX + 4.dp.toPx(), height - alturaGastado),
                size = Size(barWidth, alturaGastado),
                cornerRadius = cornerRadius
            )
        }
    }
}