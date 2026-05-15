package LosPrimos.Durango.calculadoragastos.ui.screens

import LosPrimos.Durango.calculadoragastos.data.entities.Gasto
import LosPrimos.Durango.calculadoragastos.data.enums.TipoPago
import LosPrimos.Durango.calculadoragastos.ui.components.GradientFormBackground
import LosPrimos.Durango.calculadoragastos.ui.theme.MagentaPink
import LosPrimos.Durango.calculadoragastos.ui.theme.TealDark
import LosPrimos.Durango.calculadoragastos.viewModel.GastoViewModel
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.ZoneOffset
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.produceState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AgregarGastoFijoScreen(
    onBack: () -> Unit,
    gastoViewModel: GastoViewModel,
    idGastoEditar: Int? = null
) {
    val usuarioActualId by gastoViewModel.usuarioActualId.collectAsState()
    val gastoExistente by produceState<Gasto?>(initialValue = null, idGastoEditar) {
        value = idGastoEditar?.let { gastoViewModel.obtenerGastoPorId(it) }
    }

    var monto by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var frecuenciaSeleccionada by remember { mutableStateOf("") }
    var menuFrecuencia by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val frecuencias = listOf("Diario", "Semanal", "Quincenal", "Mensual", "Anual")

    LaunchedEffect(gastoExistente) {
        gastoExistente?.let { g ->
            monto = g.monto.toString()
            descripcion = g.descripcion
            frecuenciaSeleccionada = g.lugar.ifEmpty { "Mensual" }
        }
    }

    GradientFormBackground(
        title = if (idGastoEditar == null) "Nuevo Gasto Fijo" else "Editar Gasto Fijo",
        onBack = onBack
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 30.dp, vertical = 25.dp),
                verticalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                Text("Monto", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = Color.Black)
                OutlinedTextField(
                    value = monto,
                    onValueChange = { monto = it; errorMessage = "" },
                    placeholder = { Text("$ 300", color = Color(0xFF4D4D5C), fontSize = 14.sp) },
                    modifier = Modifier.fillMaxWidth().height(58.dp),
                    shape = RoundedCornerShape(7.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White, unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = TealDark, unfocusedIndicatorColor = Color(0xFFC8C2D2),
                        cursorColor = TealDark
                    )
                )

                Text("Descripción", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = Color.Black)
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it; errorMessage = "" },
                    placeholder = { Text("Ej. Renta, Internet, Gym", color = Color(0xFF4D4D5C), fontSize = 14.sp) },
                    modifier = Modifier.fillMaxWidth().height(58.dp),
                    shape = RoundedCornerShape(7.dp),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White, unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = TealDark, unfocusedIndicatorColor = Color(0xFFC8C2D2),
                        cursorColor = TealDark
                    )
                )

                Text("Frecuencia", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = Color.Black)
                Box {
                    OutlinedButton(
                        onClick = { menuFrecuencia = true },
                        modifier = Modifier.fillMaxWidth().height(58.dp),
                        shape = RoundedCornerShape(7.dp),
                        border = BorderStroke(1.dp, Color(0xFFC8C2D2)),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
                    ) {
                        Text(text = frecuenciaSeleccionada.ifEmpty { "Selecciona la frecuencia" },
                            color = Color(0xFF4D4D5C),
                            fontSize = 14.sp,
                            modifier = Modifier.weight(1f))
                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = Color.Gray)
                    }
                    DropdownMenu(expanded = menuFrecuencia, onDismissRequest = { menuFrecuencia = false }) {
                        frecuencias.forEach { frec ->
                            DropdownMenuItem(text = { Text(frec) }, onClick = { frecuenciaSeleccionada = frec; menuFrecuencia = false })
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(top = 16.dp, bottom = 40.dp, start = 30.dp, end = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        if (usuarioActualId == null || usuarioActualId == 0) return@Button
                        val montoDouble = monto.toDoubleOrNull()
                        if (montoDouble == null || montoDouble <= 0) { errorMessage = "Monto inválido."; return@Button }
                        if (descripcion.isBlank()) { errorMessage = "La descripción no puede estar vacía."; return@Button }
                        if (frecuenciaSeleccionada.isBlank()) { errorMessage = "Selecciona una frecuencia."; return@Button }

                        val gasto = Gasto(
                            idGasto = idGastoEditar ?: 0,
                            idUsuarioPaga = usuarioActualId!!,
                            idCategoria = null, idGrupo = null, idTarjeta = null,
                            monto = montoDouble, descripcion = descripcion,
                            fecha = LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli(),
                            tipoPago = TipoPago.EFECTIVO,
                            lugar = frecuenciaSeleccionada, //para no seguir modificando entidades se opto por agregar la frecuencia en el atributo de la ubicacion (lugar)
                            fotoRecibo = null,
                            esFijo = true
                        )

                        if (idGastoEditar == null) gastoViewModel.insertarGasto(gasto) else gastoViewModel.actualizarGasto(gasto)
                        onBack()
                    },
                    modifier = Modifier.width(190.dp).height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MagentaPink),
                    shape = RoundedCornerShape(9.dp)
                ) {
                    Text(if (idGastoEditar == null) "Guardar" else "Actualizar", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                }

                AnimatedVisibility(visible = errorMessage.isNotEmpty()) {
                    Text(errorMessage, color = Color.Red, fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
                }
            }
        }
    }
}