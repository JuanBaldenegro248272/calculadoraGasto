package LosPrimos.Durango.calculadoragastos.ui.screens

import LosPrimos.Durango.calculadoragastos.data.entities.Categoria
import LosPrimos.Durango.calculadoragastos.data.entities.Gasto
import LosPrimos.Durango.calculadoragastos.data.enums.TipoPago
import LosPrimos.Durango.calculadoragastos.ui.components.FormDateField
import LosPrimos.Durango.calculadoragastos.ui.components.GradientFormBackground
import LosPrimos.Durango.calculadoragastos.ui.components.BotonAccion
import LosPrimos.Durango.calculadoragastos.ui.components.BotonPago
import LosPrimos.Durango.calculadoragastos.ui.theme.MagentaPink
import LosPrimos.Durango.calculadoragastos.ui.theme.TealDark
import LosPrimos.Durango.calculadoragastos.utils.obtenerUbicacionGPS
import LosPrimos.Durango.calculadoragastos.viewModel.CategoriaViewModel
import LosPrimos.Durango.calculadoragastos.viewModel.GastoViewModel
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.location.LocationServices
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import android.Manifest
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.produceState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AgregarGastoScreen(onBack: () -> Unit, gastoviewModel: GastoViewModel, categoriaViewModel: CategoriaViewModel, idGastoEditar: Int? = null) {
    val categorias by categoriaViewModel.obtenerCategorias().collectAsState(initial = emptyList())
    var menuCategorias by remember { mutableStateOf(false) }
    var categoriaSeleccionada by remember { mutableStateOf<Categoria?>(null) }
    val usuarioActualId by gastoviewModel.usuarioActualId.collectAsState()
    val gastoExistente by produceState<Gasto?>(initialValue = null, idGastoEditar) {
        value = idGastoEditar?.let { gastoviewModel.obtenerGastoPorId(it) }
    }
    val formato = DateTimeFormatter.ofPattern("dd / MM / yyyy")
    var monto by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var metodoPago by remember { mutableStateOf("Efectivo") }
    var fechaLong by remember {
        mutableStateOf(LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli())
    }

    var errorMessage by remember { mutableStateOf("") }

    val context = LocalContext.current
    var fotoUri by remember { mutableStateOf<Uri?>(null) }
    var lugar by remember { mutableStateOf("") }
    var isLoadingLocation by remember { mutableStateOf(false) }

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        fotoUri = uri
    }

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

    LaunchedEffect(gastoExistente) {
        gastoExistente?.let { g ->
            monto = g.monto.toString()
            descripcion = g.descripcion
            metodoPago = if (g.tipoPago == TipoPago.EFECTIVO) {
                "Efectivo"
            } else {
                "Tarjeta"
            }
            fechaLong = g.fecha
            categoriaSeleccionada =
                categorias.find { it.idCategoria == g.idCategoria }
            if (g.fotoRecibo != null) {
                fotoUri = Uri.parse(g.fotoRecibo)
            }
        }
    }

    LaunchedEffect(categorias) {
        if (categorias.isNotEmpty() && categoriaSeleccionada == null) {
            categoriaSeleccionada = categorias.first()
        }
    }


    GradientFormBackground(
        title = "Nuevo Gasto",
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
                    "Categoria",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Box {
                    OutlinedButton(
                        onClick = { menuCategorias = true },
                        modifier = Modifier.height(32.dp),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color(0xFFC8C2D2)),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color(0xFFF9F9F9),
                            contentColor = Color(0xFF202124)
                        )
                    ) {
                        Text(text = categoriaSeleccionada?.nombre ?: "Otros", fontSize = 13.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    DropdownMenu(
                        expanded = menuCategorias,
                        onDismissRequest = { menuCategorias = false }) {
                        categorias.forEach { categoria ->
                            DropdownMenuItem(
                                text = { Text(text = categoria.nombre) },
                                onClick = {
                                    categoriaSeleccionada = categoria; menuCategorias = false
                                })
                        }
                    }
                }

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

                Text(
                    "Metodo de pago",
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
                        onClick = { metodoPago = "Tarjeta" },
                        modifier = Modifier.weight(1f)
                    )
                    BotonPago(
                        texto = "Efectivo",
                        seleccionado = metodoPago == "Efectivo",
                        onClick = { metodoPago = "Efectivo" },
                        modifier = Modifier.weight(1f)
                    )
                }

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
                    .padding(top = 16.dp, bottom = 40.dp, start = 30.dp, end = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        if (usuarioActualId == null || usuarioActualId == 0) return@Button

                        if (monto.isBlank()) { errorMessage = "Debes ingresar un monto."; return@Button }
                        val montoDouble = monto.toDoubleOrNull()
                        if (montoDouble == null || montoDouble <= 0) { errorMessage = "Ingresa un monto numérico válido."; return@Button }
                        if (descripcion.isBlank()) { errorMessage = "La descripción no puede estar vacía."; return@Button }

                        errorMessage = ""

                        val gasto = Gasto(
                            idGasto = idGastoEditar ?: 0, idUsuarioPaga = usuarioActualId!!, idCategoria = categoriaSeleccionada?.idCategoria,
                            idGrupo = null, idTarjeta = null, monto = montoDouble, descripcion = descripcion, fecha = fechaLong,
                            tipoPago = if (metodoPago == "Efectivo") TipoPago.EFECTIVO else TipoPago.TARJETA, lugar = lugar, fotoRecibo = fotoUri?.toString()
                        )

                        if (idGastoEditar == null) gastoviewModel.insertarGasto(gasto) else gastoviewModel.actualizarGasto(gasto)
                        onBack()
                    },
                    modifier = Modifier.width(190.dp).height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MagentaPink),
                    shape = RoundedCornerShape(9.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text(if (idGastoEditar == null) "Guardar" else "Actualizar", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
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