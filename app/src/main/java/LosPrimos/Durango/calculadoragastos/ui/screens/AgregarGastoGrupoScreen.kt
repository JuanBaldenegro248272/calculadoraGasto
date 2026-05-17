package LosPrimos.Durango.calculadoragastos.ui.screens

import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.FileUpload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardOptions
import coil.compose.AsyncImage
import LosPrimos.Durango.calculadoragastos.data.entities.GastoGrupo
import LosPrimos.Durango.calculadoragastos.ui.components.BotonAccion
import LosPrimos.Durango.calculadoragastos.ui.components.FormDateField
import LosPrimos.Durango.calculadoragastos.ui.components.GradientFormBackground
import LosPrimos.Durango.calculadoragastos.ui.theme.MagentaPink
import LosPrimos.Durango.calculadoragastos.ui.theme.TealDark
import LosPrimos.Durango.calculadoragastos.viewModel.GastoGrupoViewModel
import androidx.compose.runtime.produceState
import java.time.LocalDate
import java.time.ZoneOffset

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AgregarGastoGrupoScreen(
    idGrupo: String,
    idGastoEditar: String? = null,
    onBack: () -> Unit,
    gastoGrupoViewModel: GastoGrupoViewModel
) {
    val usuarioActualId by gastoGrupoViewModel.usuarioActualId.collectAsState()

    val gastoExistente by produceState<GastoGrupo?>(initialValue = null, idGastoEditar) {
        value = if (idGastoEditar != null) {
            gastoGrupoViewModel.gastosGrupo.find { it.idGastoGrupo == idGastoEditar }
        } else null
    }

    var monto by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var menuCategorias by remember { mutableStateOf(false) }
    var categoriaSeleccionada by remember { mutableStateOf("Alimentacion") }
    var fechaLong by remember {
        mutableStateOf(LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli())
    }
    var fotoUri by remember { mutableStateOf<Uri?>(null) }
    var errorMessage by remember { mutableStateOf("") }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> fotoUri = uri }

    LaunchedEffect(gastoExistente) {
        gastoExistente?.let { g ->
            monto = g.montoTotal.toString()
            descripcion = g.descripcion
            categoriaSeleccionada = when (g.idCategoria) {
                2 -> "Entretenimiento"
                3 -> "Transporte"
                4 -> "Alimentacion"
                5 -> "Salud"
                else -> "Alimentacion"
            }
            fechaLong = g.fecha
            if (!g.fotoRecibo.isNullOrBlank()) fotoUri = Uri.parse(g.fotoRecibo)
        }
    }

    GradientFormBackground(
        title = if (idGastoEditar == null) "Nuevo Gasto" else "Editar Gasto",
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

                // Monto
                Text("Monto", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = Color.Black)
                OutlinedTextField(
                    value = monto,
                    onValueChange = { monto = it; errorMessage = "" },
                    placeholder = { Text("Ej. 500.00", color = Color(0xFF4D4D5C), fontSize = 14.sp) },
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

                Text("Categoria", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = Color.Black)
                Box {
                    OutlinedButton(
                        onClick = { menuCategorias = true },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(7.dp)
                    ) {
                        Text(
                            text = categoriaSeleccionada,
                            color = Color(0xFF363645),
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Seleccionar categoria"
                        )
                    }

                    DropdownMenu(
                        expanded = menuCategorias,
                        onDismissRequest = { menuCategorias = false }
                    ) {
                        listOf("Salud", "Alimentacion", "Transporte", "Entretenimiento").forEach { categoria ->
                            DropdownMenuItem(
                                text = { Text(categoria) },
                                onClick = {
                                    categoriaSeleccionada = categoria
                                    menuCategorias = false
                                }
                            )
                        }
                    }
                }

                // Descripcion
                Text("Descripcion", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = Color.Black)
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it; errorMessage = "" },
                    placeholder = { Text("Ej. Cena en el restaurante", color = Color(0xFF4D4D5C), fontSize = 14.sp) },
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

                // Fecha
                FormDateField(
                    label = "Fecha",
                    fechaMilisegundos = fechaLong,
                    onDateSelected = { fechaLong = it }
                )

                // Comprobante imagen
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

            // botonn guardar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 40.dp, start = 30.dp, end = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        if (usuarioActualId.isNullOrBlank()) return@Button
                        if (monto.isBlank()) { errorMessage = "Debes ingresar un monto."; return@Button }
                        val montoDouble = monto.toDoubleOrNull()
                        if (montoDouble == null || montoDouble <= 0) {
                            errorMessage = "Ingresa un monto numérico válido."
                            return@Button
                        }
                        if (descripcion.isBlank()) { errorMessage = "La descripción no puede estar vacía."; return@Button }

                        errorMessage = ""

                        val gasto = GastoGrupo(
                            idGastoGrupo = idGastoEditar ?: "",
                            idGrupo      = idGrupo,
                            idUsuarioPagador = usuarioActualId!!,
                            montoTotal   = montoDouble,
                            idCategoria = when (categoriaSeleccionada) {
                                "Salud" -> 5
                                "Transporte" -> 3
                                "Entretenimiento" -> 2
                                else -> 4
                            },
                            descripcion  = descripcion,
                            fecha        = fechaLong,
                            fotoRecibo   = fotoUri?.toString()
                        )

                        if (idGastoEditar == null) {
                            gastoGrupoViewModel.agregarGasto(gasto)
                        } else {
                            gastoGrupoViewModel.actualizarGasto(gasto)
                        }
                        onBack()
                    },
                    modifier = Modifier.width(190.dp).height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MagentaPink),
                    shape = RoundedCornerShape(9.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text(
                        text = if (idGastoEditar == null) "Guardar" else "Actualizar",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
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
