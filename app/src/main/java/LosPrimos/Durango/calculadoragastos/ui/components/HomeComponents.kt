package LosPrimos.Durango.calculadoragastos.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import LosPrimos.Durango.calculadoragastos.ui.theme.*
import android.R.attr.timeZone
import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import androidx.compose.ui.platform.LocalLocale


@Composable
private fun MiniBalanceCard(
    titulo: String,
    monto: Double,
    icono: ImageVector,
    colorIcono: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceWhite.copy(alpha = 0.4f)
        ),
        modifier = modifier

    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icono, contentDescription = null, tint = colorIcono, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(titulo, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = SurfaceWhite)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("$${monto}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = SurfaceWhite)
        }
    }
}




@Composable
fun BalanceSummarySection(
    ingresos: Double,
    gastos: Double,
    balance: Double,
    presupuestoUtilizado: Float,
    textoPresupuesto: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MiniBalanceCard("Ingresos", ingresos, Icons.Default.ArrowUpward, DarkGrayText, Modifier.weight(1f))
            Spacer(modifier = Modifier.width(8.dp))
            MiniBalanceCard("Gastos", gastos, Icons.Default.ArrowDownward, DarkGrayText, Modifier.weight(1f))
            Spacer(modifier = Modifier.width(8.dp))
            MiniBalanceCard("Balance", balance, Icons.Default.Menu, DarkGrayText, Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = textoPresupuesto,
            color = Color.White,
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        LinearProgressIndicator(
            progress = { presupuestoUtilizado },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = MagentaPink,
            trackColor = DarkGrayText
        )
    }
}









@Composable
fun BottomRoundedSurface(content: @Composable () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        color = SurfaceWhite
    ) {
        content()
    }
}


@Composable
private fun CustomDropdownSelector(
    label: String,
    items: List<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {
    var expandido by remember { mutableStateOf(false) }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            color = DarkGrayText,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.width(6.dp))

        Box {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { expandido = true }
            ) {
                Text(
                    text = selectedItem,
                    color = TealDark,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Seleccionar $label",
                    tint = TealDark
                )
            }

            DropdownMenu(
                expanded = expandido,
                onDismissRequest = { expandido = false },
                modifier = Modifier.background(SurfaceWhite)
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = item,
                                color = if (item == selectedItem) TealDark else DarkGrayText,
                                fontWeight = if (item == selectedItem) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        onClick = {
                            onItemSelected(item)
                            expandido = false
                        }
                    )
                }
            }
        }
    }
}



@Composable
private fun TransactionTypeToggle(
    isGastosSelected: Boolean,
    onGastosClick: () -> Unit,
    onIngresosClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp))
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(if (isGastosSelected) MagentaPink else Color.Transparent)
                .clickable { onGastosClick() }
                .padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Gastos",
                color = if (isGastosSelected) Color.White else DarkGrayText,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(if (!isGastosSelected) MagentaPink else Color.Transparent)
                .clickable { onIngresosClick() }
                .padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Ingresos",
                color = if (!isGastosSelected) Color.White else DarkGrayText,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}


@Composable
fun TransactionsFilterSection(
    isGastosSelected: Boolean,
    onGastosClick: () -> Unit,
    onIngresosClick: () -> Unit,
    mesSeleccionado: String,
    onMesSeleccionado: (String) -> Unit,
    categoriaSeleccionada: String,
    onCategoriaSeleccionada: (String) -> Unit,
    listaCategoriasNombres: List<String>
) {
    val meses = listOf("Todos", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(BackgroundLight, RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        TransactionTypeToggle(
            isGastosSelected = isGastosSelected,
            onGastosClick = onGastosClick,
            onIngresosClick = onIngresosClick
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomDropdownSelector(
                label = "Mes",
                items = meses,
                selectedItem = mesSeleccionado,
                onItemSelected = onMesSeleccionado
            )

            if (isGastosSelected) {
                CustomDropdownSelector(
                    label = "Categoría",
                    items = listaCategoriasNombres,
                    selectedItem = categoriaSeleccionada,
                    onItemSelected = onCategoriaSeleccionada
                )
            }
        }
    }
}




@Composable
fun CategoryGroup(
    nombreCategoria: String,
    iconoCategoria: ImageVector?,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        ) {
            if (iconoCategoria != null) {
                Icon(
                    imageVector = iconoCategoria,
                    contentDescription = nombreCategoria,
                    tint = DarkGrayText,
                    modifier = Modifier.size(26.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }

            Text(
                text = nombreCategoria.uppercase(),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                color = DarkGrayText,
                letterSpacing = 1.sp
            )
        }

        Divider(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
            color = LightBlueGray.copy(alpha = 0.5f),
            thickness = 1.dp
        )

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            content()
        }
    }
}

@Composable
fun OfflineStatusBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            color = DarkGrayText.copy(alpha = 0.5f),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "SIN CONEXIÓN",
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun OfflineListWarning() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = LightBlueGray),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CloudOff,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = "Estas sin conexión",
                    fontWeight = FontWeight.Bold,
                    color = DarkGrayText,
                    fontSize = 14.sp
                )
                Text(
                    text = "Tus datos estan almacenados localmente, se sincronizaran con la nube en cuanto tengas conexion",
                    color = DarkGrayText,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun FormDateField(
    label: String,
    fechaMilisegundos: Long,
    onDateSelected: (Long) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    val sdf = SimpleDateFormat("dd/MM/yyyy", LocalLocale.current.platformLocale).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    val fechaFormateada = if (fechaMilisegundos > 0L) sdf.format(Date(fechaMilisegundos)) else "Seleccionar fecha"

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = Color.Black)
        Spacer(modifier = Modifier.height(7.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = fechaFormateada,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.fillMaxWidth().height(58.dp),
                shape = RoundedCornerShape(7.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = TealDark,
                    unfocusedIndicatorColor = Color(0xFFC8C2D2),
                    focusedTextColor = Color(0xFF363645),
                    unfocusedTextColor = Color(0xFF363645)
                )
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { showDialog = true }
                    .background(Color.Transparent)
            )
        }
    }

    if (showDialog) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = fechaMilisegundos)
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { onDateSelected(it) }
                    showDialog = false
                }) { Text("Aceptar", color = TealDark) }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Cancelar", color = DarkGrayText) }
            },
            colors = DatePickerDefaults.colors(containerColor = Color.White)
        ) {
            DatePicker(state = datePickerState)
        }
    }
}