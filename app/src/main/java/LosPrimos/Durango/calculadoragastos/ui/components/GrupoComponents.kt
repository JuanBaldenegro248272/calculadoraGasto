package LosPrimos.Durango.calculadoragastos.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import LosPrimos.Durango.calculadoragastos.ui.theme.*
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.ui.window.Dialog
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

@Composable
fun GroupsTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Mis Grupos",
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )
    }
}

@Composable
fun GroupActionButtons(
    onCreateClick: () -> Unit,
    onJoinClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = onJoinClick,
            modifier = Modifier
                .weight(1f)
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            border = null,
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = MagentaPink,
                contentColor = Color.White
            )
        ) {
            Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Unirse a Grupo", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }

        OutlinedButton(
            onClick = onCreateClick,
            modifier = Modifier
                .weight(1f)
                .height(50.dp)
                ,
            shape = RoundedCornerShape(12.dp),
            border = null,
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = TealDark,
                contentColor = Color.White
            )
        ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Crear Grupo", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }
    }
}

@Composable
fun GroupCardItemViejo(
    titulo: String,
    categoriaGrupo: String,
    cantidadMiembros: Int,
    montoTotal: Double,
    codigoGrupo: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = titulo,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    color = DarkGrayText
                )

                Text(
                    text = categoriaGrupo,
                    color = MagentaPink,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 2.dp, bottom = 4.dp)
                )

                Text(
                    text = "$cantidadMiembros Miembros",
                    color = LightBlueGray,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    color = BackgroundLight,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Código: $codigoGrupo",
                        color = TealDark,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "Total Gastado",
                    color = LightBlueGray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$${montoTotal}",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                    color = DarkGrayText
                )
            }
        }
    }
}

@Composable
fun GroupCardItem(
    titulo: String,
    categoriaGrupo: String,
    cantidadMiembros: Int,
    montoTotal: Double,
    codigoGrupo: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, LightBlueGray.copy(alpha = 0.55f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(26.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = titulo,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    Text(
                        text = categoriaGrupo,
                        color = DarkGrayText.copy(alpha = 0.75f),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Surface(
                    color = DarkGrayText,
                    shape = RoundedCornerShape(14.dp),
                    shadowElevation = 4.dp
                ) {
                    Text(
                        text = "$cantidadMiembros Miembros",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(54.dp))

            FilaDatoGrupo(
                titulo = "Total Gastado",
                valor = "$${"%.2f".format(montoTotal)}",
                colorValor = DarkGrayText.copy(alpha = 0.65f)
            )

            Spacer(modifier = Modifier.height(14.dp))

            FilaDatoGrupo(
                titulo = "Mi deuda",
                valor = "$0.00",
                colorValor = MagentaPink
            )

            Divider(
                modifier = Modifier.padding(vertical = 14.dp),
                color = LightBlueGray.copy(alpha = 0.35f),
                thickness = 4.dp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Codigo: $codigoGrupo",
                    color = DarkGrayText,
                    fontSize = 14.sp
                )

                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Copiar codigo",
                    tint = DarkGrayText.copy(alpha = 0.75f),
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}

@Composable
private fun FilaDatoGrupo(
    titulo: String,
    valor: String,
    colorValor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = titulo,
            color = DarkGrayText,
            fontSize = 14.sp
        )
        Text(
            text = valor,
            color = colorValor,
            fontSize = 15.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}


@Composable
fun JoinGroupDialog(
    onDismiss: () -> Unit,
    onJoinConfirm: (String) -> Unit
) {
    var codigo by remember { mutableStateOf("") }

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
                    text = "Unirse a Grupo",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                    color = DarkGrayText
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = codigo,
                    onValueChange = { codigo = it.uppercase() },
                    label = { Text("Código del grupo") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TealDark,
                        unfocusedBorderColor = LightBlueGray,
                        focusedTextColor = DarkGrayText
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

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
                        onClick = { onJoinConfirm(codigo) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = TealDark)
                    ) {
                        Text("Unirse", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGroupDialog(
    onDismiss: () -> Unit,
    onCreateConfirm: (nombre: String, categoria: String, codigo: String, imagenUri: String) -> Unit
) {
    var nombreGrupo by remember { mutableStateOf("") }
    val codigoGrupo = remember { (10000..999999).random().toString() }
    val categorias = listOf("Familia", "Pareja", "Viaje", "Amigos", "Otro")
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var categoriaSeleccionada by remember { mutableStateOf(categorias[0]) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

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
                    text = "Crear Grupo",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                    color = DarkGrayText
                )

                Spacer(modifier = Modifier.height(24.dp))

                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEEEEEE))
                        .clickable { galleryLauncher.launch("image/*") }, // Abrir galería al tocar
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedImageUri != null) {
                        AsyncImage(
                            model = selectedImageUri,
                            contentDescription = "Foto del grupo",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Agregar imagen",
                            tint = Color.Gray,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = nombreGrupo,
                    onValueChange = { nombreGrupo = it },
                    label = { Text("Nombre de grupo") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TealDark,
                        unfocusedBorderColor = LightBlueGray,
                        focusedTextColor = DarkGrayText
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                ExposedDropdownMenuBox(
                    expanded = isDropdownExpanded,
                    onExpandedChange = { isDropdownExpanded = !isDropdownExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = categoriaSeleccionada,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Tipo de grupo") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded) },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = TealDark,
                            unfocusedBorderColor = LightBlueGray,
                            focusedTextColor = DarkGrayText
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = isDropdownExpanded,
                        onDismissRequest = { isDropdownExpanded = false },
                        modifier = Modifier.background(Color.White)
                    ) {
                        categorias.forEach { categoria ->
                            DropdownMenuItem(
                                text = { Text(categoria, color = DarkGrayText) },
                                onClick = {
                                    categoriaSeleccionada = categoria
                                    isDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

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
                            if(nombreGrupo.isNotBlank()) {
                                onCreateConfirm(nombreGrupo, categoriaSeleccionada, codigoGrupo, selectedImageUri?.toString() ?: "")
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = TealDark)
                    ) {
                        Text("Crear", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
