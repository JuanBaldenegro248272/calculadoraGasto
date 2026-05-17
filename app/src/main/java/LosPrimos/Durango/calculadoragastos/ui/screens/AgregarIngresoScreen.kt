package LosPrimos.Durango.calculadoragastos.ui.screens

import LosPrimos.Durango.calculadoragastos.data.entities.Ingreso
import LosPrimos.Durango.calculadoragastos.ui.components.BotonAccion
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
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.FileUpload
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
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

    var errorMessage by remember { mutableStateOf("") }

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
            if (i.fotoRecibo != null) {
                fotoUri = Uri.parse(i.fotoRecibo)
            }
        }
    }

    GradientFormBackground(
        title = "Nuevo Ingreso",
        onBack = onBack
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 30.dp, vertical = 25.dp),
                verticalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                Text(
                    "Monto",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = Color.Black
                )
                OutlinedTextField(
                    value = monto,
                    onValueChange = { monto = it; errorMessage = "" },
                    placeholder = {
                        Text(
                            text = "$ 300",
                            color = Color(0xFF4D4D5C),
                            fontSize = 14.sp
                        )
                    },
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

                Text(
                    "Descripcion",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = Color.Black
                )
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it; errorMessage = "" },
                    placeholder = {
                        Text(
                            text = "Ej. Compra en el aurrera",
                            color = Color(0xFF4D4D5C),
                            fontSize = 14.sp
                        )
                    },
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

                FormDateField(
                    label = "Fecha",
                    fechaMilisegundos = fechaLong,
                    onDateSelected = { fechaLong = it })

                Text(
                    text = "Ubicacion (Opcional)",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = Color.Black
                )
                BotonAccion(
                    texto = if (isLoadingLocation) "Buscando satélites..." else if (lugar.isNotEmpty()) lugar.take(30) + "..." else "Capturar Ubicacion",
                    icono = if (lugar.isNotEmpty() && !isLoadingLocation && lugar != "Permiso denegado") Icons.Default.CheckCircle else Icons.Outlined.LocationOn,
                    onClick = {
                        locationPermissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Comprobante (Opcional)",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = Color.Black
                )

                BotonAccion(
                    texto = if (fotoUri != null) "Cambiar Foto" else "Subir Foto",
                    icono = if (fotoUri != null) Icons.Default.CheckCircle else Icons.Outlined.FileUpload,
                    onClick = { galleryLauncher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth()
                )

                if (fotoUri != null) {
                    AsyncImage(
                        model = fotoUri,
                        contentDescription = "Vista previa del comprobante",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, Color(0xFFC8C2D2), RoundedCornerShape(8.dp))
                    )
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
                        if (usuarioActualId.value == null) return@Button

                        if (monto.isBlank()) {
                            errorMessage = "Debes ingresar un monto."
                            return@Button
                        }
                        val montoDouble = monto.toDoubleOrNull()
                        if (montoDouble == null || montoDouble <= 0) {
                            errorMessage = "Ingresa un monto numérico válido."
                            return@Button
                        }
                        if (descripcion.isBlank()) {
                            errorMessage = "La descripción no puede estar vacía."
                            return@Button
                        }

                        errorMessage = ""

                        val uriFinal = if (fotoUri != null) saveImageToInternalStorage(context, fotoUri!!) else null

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

                        if (idIngresoEditar == null) ingresoViewModel.insertarIngreso(nuevoIngreso) else ingresoViewModel.actualizarIngreso(nuevoIngreso)
                        onBack()
                    },
                    modifier = Modifier.width(190.dp).height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MagentaPink),
                    shape = RoundedCornerShape(9.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text(if (idIngresoEditar == null) "Guardar" else "Actualizar", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                }

                AnimatedVisibility(visible = errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}