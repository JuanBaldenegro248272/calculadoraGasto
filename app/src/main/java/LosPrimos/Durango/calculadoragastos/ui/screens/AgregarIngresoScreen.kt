package LosPrimos.Durango.calculadoragastos.ui.screens

import LosPrimos.Durango.calculadoragastos.data.entities.Ingreso
import LosPrimos.Durango.calculadoragastos.ui.components.GradientFormBackground
import LosPrimos.Durango.calculadoragastos.ui.theme.MagentaPink
import LosPrimos.Durango.calculadoragastos.ui.theme.TealDark
import LosPrimos.Durango.calculadoragastos.viewModel.IngresoViewModel
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.FileUpload
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AgregarIngresoScreen(onBack: () -> Unit, ingresoViewModel: IngresoViewModel) {
    val usuarioActualId = ingresoViewModel.usuarioActualId.collectAsState()
    val formato = DateTimeFormatter.ofPattern("dd / MM / yyyy")
    var monto by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf(LocalDate.now().format(formato)) }

    GradientFormBackground(
        title = "Nuevo Ingreso",
        onBack = onBack
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 34.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(9.dp)
        ) {
            Text(
                text = "Monto",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                color = Color.Black
            )
            OutlinedTextField(
                value = monto,
                onValueChange = { monto = it },
                placeholder = {
                    Text(
                        text = "$ 300",
                        color = Color(0xFF4D4D5C),
                        fontSize = 14.sp
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(7.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = TealDark,
                    unfocusedIndicatorColor = Color(0xFFC8C2D2),
                    focusedTextColor = Color(0xFF363645),
                    unfocusedTextColor = Color(0xFF363645),
                    cursorColor = TealDark
                )
            )

            Text(
                text = "Categoria",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                color = Color.Black
            )
            OutlinedButton(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color(0xFFFDFDFD),
                    contentColor = Color(0xFF202124)
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "No Fijo", fontSize = 13.sp)
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Seleccionar categoria",
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Text(
                text = "Descripcion",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                color = Color.Black
            )
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                placeholder = {
                    Text(
                        text = "Ej. Compra en el aurrera",
                        color = Color(0xFF4D4D5C),
                        fontSize = 14.sp
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(7.dp),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = TealDark,
                    unfocusedIndicatorColor = Color(0xFFC8C2D2),
                    focusedTextColor = Color(0xFF363645),
                    unfocusedTextColor = Color(0xFF363645),
                    cursorColor = TealDark
                )
            )

            Text(
                text = "Fecha",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                color = Color.Black
            )
            OutlinedTextField(
                value = fecha,
                onValueChange = { fecha = it },
                placeholder = {
                    Text(
                        text = fecha,
                        color = Color(0xFF4D4D5C),
                        fontSize = 14.sp
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(7.dp),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = TealDark,
                    unfocusedIndicatorColor = Color(0xFFC8C2D2),
                    focusedTextColor = Color(0xFF363645),
                    unfocusedTextColor = Color(0xFF363645),
                    cursorColor = TealDark
                )
            )

            Text(
                text = "Ubicacion (Opcional)",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                color = Color.Black
            )
            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .border(1.dp, Color(0xFFC8C2D2), RoundedCornerShape(7.dp)),
                shape = RoundedCornerShape(7.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF363645)
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(27.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Capturar Ubicacion", fontSize = 14.sp)
            }

            Text(
                text = "Foto del Recibo (Opcional)",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                color = Color.Black
            )
            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .border(1.dp, Color(0xFFC8C2D2), RoundedCornerShape(7.dp)),
                shape = RoundedCornerShape(7.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF363645)
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.FileUpload,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(27.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Subir Foto", fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(

                onClick = {
                    if (usuarioActualId.value == null){
                        return@Button
                    }

                    val localDate = LocalDate.parse(fecha, formato)
                    val fechaLong = localDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
                    val nuevoIngreso = Ingreso(
                        idIngreso = 0,
                        monto = monto.toDouble(),
                        fecha = fechaLong,
                        descripcion = descripcion,
                        idCategoria = null,
                        idUsuario = usuarioActualId.value
                    )

                    ingresoViewModel.insertarIngreso(nuevoIngreso)
                    onBack()
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(190.dp)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MagentaPink),
                shape = RoundedCornerShape(9.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text(
                    text = "Guardar",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(130.dp))
        }
    }
}

