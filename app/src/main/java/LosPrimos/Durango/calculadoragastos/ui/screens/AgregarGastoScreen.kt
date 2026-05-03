package LosPrimos.Durango.calculadoragastos.ui.screens

import LosPrimos.Durango.calculadoragastos.ui.components.GradientFormBackground
import LosPrimos.Durango.calculadoragastos.ui.theme.MagentaPink
import LosPrimos.Durango.calculadoragastos.ui.theme.TealDark
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AgregarGastoScreen(onBack: () -> Unit) {
    val formato = DateTimeFormatter.ofPattern("dd / MM / yyyy")
    var monto by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var metodoPago by remember { mutableStateOf("Efectivo") }
    var fecha by remember { mutableStateOf(LocalDate.now().format(formato)) }

    GradientFormBackground(
        title = "Nuevo Gasto",
        onBack = onBack
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(7.dp)
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
                modifier = Modifier.height(32.dp),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color(0xFFC8C2D2)),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color(0xFFF9F9F9),
                    contentColor = Color(0xFF202124)
                )
            ) {
                Text(text = "Alimentacion", fontSize = 13.sp)
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Seleccionar categoria",
                    modifier = Modifier.size(18.dp)
                )
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
                text = "Metodo de pago",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                color = Color.Black
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(44.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                BotonPago(
                    texto = "Tarjeta",
                    seleccionado = metodoPago == "Tarjeta",
                    alDarClick = { metodoPago = "Tarjeta" },
                    modifier = Modifier.weight(1f)
                )
                BotonPago(
                    texto = "Efectivo",
                    seleccionado = metodoPago == "Efectivo",
                    alDarClick = { metodoPago = "Efectivo" },
                    modifier = Modifier.weight(1f)
                )
            }

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
                        text = fecha.toString(),
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
            BotonAccion(
                texto = "Capturar Ubicacion",
                icono = Icons.Outlined.LocationOn
            )

            Text(
                text = "Foto del Recibo (Opcional)",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                color = Color.Black
            )
            BotonAccion(
                texto = "Subir Foto",
                icono = Icons.Outlined.FileUpload
            )

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = {},
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
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun BotonPago(
    texto: String,
    seleccionado: Boolean,
    alDarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = alDarClick,
        modifier = modifier.height(40.dp),
        shape = RoundedCornerShape(9.dp),
        border = if (seleccionado) null else BorderStroke(1.dp, Color(0xFFC8C2D2)),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (seleccionado) TealDark else Color(0xFFF3F3F3),
            contentColor = if (seleccionado) Color.White else Color(0xFF353541)
        )
    ) {
        Text(text = texto, fontSize = 14.sp)
    }
}

@Composable
private fun BotonAccion(
    texto: String,
    icono: ImageVector
) {
    Button(
        onClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .height(42.dp)
            .border(1.dp, Color(0xFFC8C2D2), RoundedCornerShape(7.dp)),
        shape = RoundedCornerShape(7.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color(0xFF363645)
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(27.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = texto, fontSize = 14.sp)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AgregarGastoScreenPreview() {
    AgregarGastoScreen(onBack = {})
}
