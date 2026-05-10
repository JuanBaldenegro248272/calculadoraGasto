package LosPrimos.Durango.calculadoragastos.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import LosPrimos.Durango.calculadoragastos.ui.theme.*
import androidx.compose.ui.window.Dialog

@Composable
fun PresupuestoTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Presupuesto Mensual",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )
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
fun PresupuestoToggle(
    isCategoriaSelected: Boolean,
    onCategoriaClick: () -> Unit,
    onFijosClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .background(BackgroundLight, RoundedCornerShape(12.dp))
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(if (isCategoriaSelected) MagentaPink else Color.Transparent)
                .clickable { onCategoriaClick() }
                .padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Por Categoría",
                color = if (isCategoriaSelected) Color.White else DarkGrayText,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
        }

        Box(
            modifier = Modifier
                .weight(1.2f)
                .clip(RoundedCornerShape(8.dp))
                .background(if (!isCategoriaSelected) MagentaPink else Color.Transparent)
                .clickable { onFijosClick() }
                .padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Ingresos / Gastos Fijos",
                color = if (!isCategoriaSelected) Color.White else DarkGrayText,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
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
fun AddFixedTransactionDialog(
    isGasto: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (nombre: String, monto: Double, dia: Int) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var monto by remember { mutableStateOf("") }
    var dia by remember { mutableStateOf("") }

    val themeColor = if (isGasto) MagentaPink else TealDark
    val title = if (isGasto) "Añadir Gasto Fijo" else "Añadir Ingreso Fijo"
    val nameLabel = if (isGasto) "Nombre de gasto fijo" else "Nombre de ingreso fijo"

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                    color = DarkGrayText
                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text(nameLabel) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = themeColor,
                        unfocusedBorderColor = LightBlueGray,
                        focusedTextColor = DarkGrayText
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = monto,
                    onValueChange = { monto = it },
                    label = { Text("Monto") },
                    leadingIcon = { Text("$", color = DarkGrayText) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = themeColor,
                        unfocusedBorderColor = LightBlueGray,
                        focusedTextColor = DarkGrayText
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = dia,
                    onValueChange = { dia = it },
                    label = { Text("Día del mes (Ej. 15)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = themeColor,
                        unfocusedBorderColor = LightBlueGray,
                        focusedTextColor = DarkGrayText
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DarkGrayText)
                    ) {
                        Text("Cancelar", color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = {
                            val montoParsed = monto.toDoubleOrNull() ?: 0.0
                            val diaParsed = dia.toIntOrNull() ?: 1
                            if (nombre.isNotBlank() && montoParsed > 0) {
                                onConfirm(nombre, montoParsed, diaParsed)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = themeColor)
                    ) {
                        Text("Añadir", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}