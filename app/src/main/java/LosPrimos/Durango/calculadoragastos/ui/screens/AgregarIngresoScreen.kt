package LosPrimos.Durango.calculadoragastos.ui.screens

import LosPrimos.Durango.calculadoragastos.data.entities.Gasto
import LosPrimos.Durango.calculadoragastos.data.entities.Ingreso
import LosPrimos.Durango.calculadoragastos.data.enums.TipoPago
import LosPrimos.Durango.calculadoragastos.ui.components.FormDateField
import LosPrimos.Durango.calculadoragastos.ui.components.GradientFormBackground
import LosPrimos.Durango.calculadoragastos.ui.theme.MagentaPink
import LosPrimos.Durango.calculadoragastos.ui.theme.TealDark
import LosPrimos.Durango.calculadoragastos.utils.obtenerUbicacionGPS
import LosPrimos.Durango.calculadoragastos.utils.saveImageToInternalStorage
import LosPrimos.Durango.calculadoragastos.viewModel.IngresoViewModel
import android.Manifest
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.CheckCircle
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.location.LocationServices
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AgregarIngresoScreen(onBack: () -> Unit, ingresoViewModel: IngresoViewModel, idIngresoEditar: Int? = null) {
    val usuarioActualId = ingresoViewModel.usuarioActualId.collectAsState()
    val formato = DateTimeFormatter.ofPattern("dd / MM / yyyy")
    var monto by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var fechaLong by remember {
        mutableStateOf(LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli())
    }
    val ingresoExistente by produceState<Ingreso?>(initialValue = null, idIngresoEditar) {
        value = idIngresoEditar?.let { ingresoViewModel.obtenerIngresoPorId(it) }
    }

    val context = LocalContext.current
    var fotoUri by remember { mutableStateOf<Uri?>(null) }
    var lugar by remember { mutableStateOf("") }
    var isLoadingLocation by remember { mutableStateOf(false) }

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> fotoUri = uri }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            isLoadingLocation = true
            lugar = "Buscando satélites..."
            obtenerUbicacionGPS(context, fusedLocationClient) { direccion ->
                lugar = direccion
                isLoadingLocation = false
            }
        } else {
            lugar = "Permiso denegado"
            isLoadingLocation = false
        }
    }

    LaunchedEffect(ingresoExistente) {
        ingresoExistente?.let { i ->
            monto = i.monto.toString()
            descripcion = i.descripcion ?: ""
            fechaLong = i.fecha
        }
    }

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
            Text(text = "Monto", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = Color.Black)
            OutlinedTextField(
                value = monto,
                onValueChange = { monto = it },
                placeholder = { Text(text = "$ 300", color = Color(0xFF4D4D5C), fontSize = 14.sp) },
                modifier = Modifier.fillMaxWidth().height(58.dp),
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


            Text(text = "Descripcion", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = Color.Black)
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                placeholder = { Text(text = "Ej. Quincena", color = Color(0xFF4D4D5C), fontSize = 14.sp) },
                modifier = Modifier.fillMaxWidth().height(58.dp),
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

            FormDateField(label = "Fecha", fechaMilisegundos = fechaLong, onDateSelected = { fechaLong = it })

            Text(text = "Ubicacion (Opcional)", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = Color.Black)
            BotonAccion(
                texto = if (isLoadingLocation) "Buscando satélites..." else if (lugar.isNotEmpty()) lugar.take(30) + "..." else "Capturar Ubicacion Actual",
                icono = if (lugar.isNotEmpty() && !isLoadingLocation && lugar != "Permiso denegado") Icons.Default.CheckCircle else Icons.Outlined.LocationOn,
                onClick = {
                    locationPermissionLauncher.launch(
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                    )
                }
            )

            Text(text = "Foto del Recibo (Opcional)", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = Color.Black)
            BotonAccion(
                texto = if (fotoUri != null) "Foto adjuntada" else "Subir Foto",
                icono = if (fotoUri != null) Icons.Default.CheckCircle else Icons.Outlined.FileUpload,
                onClick = { galleryLauncher.launch("image/*") }
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    if (usuarioActualId.value == null) return@Button
                    val montoDouble = monto.toDoubleOrNull()
                    if (montoDouble == null) return@Button

                    val uriFinal = if (fotoUri != null) {
                        saveImageToInternalStorage(context, fotoUri!!)
                    } else null

                    val nuevoIngreso = Ingreso(
                        idIngreso = idIngresoEditar ?: 0,
                        monto = montoDouble,
                        fecha = fechaLong,
                        descripcion = descripcion,
                        idCategoria = null,
                        idUsuario = usuarioActualId.value!!,
                        lugar = lugar,
                        fotoRecibo = uriFinal?.toString()
                    )

                    if (idIngresoEditar == null) {
                        ingresoViewModel.insertarIngreso(nuevoIngreso)
                    } else {
                        ingresoViewModel.actualizarIngreso(nuevoIngreso)
                    }
                    onBack()
                },
                modifier = Modifier.align(Alignment.CenterHorizontally).width(190.dp).height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MagentaPink),
                shape = RoundedCornerShape(9.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text(if (idIngresoEditar == null)
                    "Guardar"
                else
                    "Actualizar",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White)
            }

            Spacer(modifier = Modifier.height(130.dp))
        }
    }
}


@Composable
private fun BotonAccion(texto: String, icono: ImageVector, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(42.dp).border(1.dp, Color(0xFFC8C2D2), RoundedCornerShape(7.dp)),
        shape = RoundedCornerShape(7.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color(0xFF363645)),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icono, contentDescription = null, tint = Color.Black, modifier = Modifier.size(27.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = texto, fontSize = 14.sp)
        }
    }
}