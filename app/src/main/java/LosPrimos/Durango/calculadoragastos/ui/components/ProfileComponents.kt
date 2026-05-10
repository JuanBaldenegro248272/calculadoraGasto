package LosPrimos.Durango.calculadoragastos.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import LosPrimos.Durango.calculadoragastos.ui.theme.*
import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
@Composable
fun ProfileTopBar(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Regresar",
                tint = Color.White
            )
        }
        Text(
            text = "Mi perfil",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
fun ProfileAvatarSection(nombre: String, correo: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Avatar del usuario",
            tint = Color.White.copy(alpha = 0.9f),
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = nombre,
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )
        Text(
            text = correo,
            fontSize = 14.sp,
            color = Color.White.copy(alpha = 0.8f)
        )
    }
}

@Composable
fun ProfileSectionHeader(
    title: String,
    isEditing: Boolean,
    onEditClick: () -> Unit,
    onCancelClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TealDark
        )

        if (isEditing) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = onCancelClick,
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Text("Cancelar", color = LightBlueGray, fontSize = 13.sp)
                }

                Button(
                    onClick = onSaveClick,
                    colors = ButtonDefaults.buttonColors(containerColor = MagentaPink),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(32.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                ) {
                    Text("Guardar", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        } else {
            TextButton(
                onClick = onEditClick,
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp), tint = TealDark)
                    Spacer(Modifier.width(4.dp))
                    Text("Editar", color = TealDark, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}



@Composable
fun ProfileSectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 16.sp,
        fontWeight = FontWeight.ExtraBold,
        color = TealDark,
        modifier = Modifier.padding(vertical = 12.dp)
    )
}

@Composable
fun ProfileFieldItem(
    label: String,
    value: String,
    isEditing: Boolean,
    onValueChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = DarkGrayText
        )
        Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = isEditing,
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = TealDark,
                unfocusedBorderColor = LightBlueGray,
                focusedTextColor = DarkGrayText,
                unfocusedTextColor = DarkGrayText,
                disabledBorderColor = Color.Transparent,
                disabledTextColor = DarkGrayText,
                disabledContainerColor = Color.Transparent
            )
        )
    }
}

@Composable
fun ProfileSwitchItem(
    title: String,
    icon: ImageVector,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = DarkGrayText,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = DarkGrayText,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = TealLight
            )
        )
    }
}

@Composable
fun ProfileLogoutButton(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.ExitToApp,
            contentDescription = "Cerrar sesión",
            tint = ErrorRed,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "Cerrar sesión",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = ErrorRed
        )
    }
}

@Composable
fun ProfileActionButtons(
    isEditing: Boolean,
    onEditClick: () -> Unit,
    onCancelClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    if (isEditing) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = onCancelClick,
                modifier = Modifier.weight(1f).height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = DarkGrayText)
            ) {
                Text("Cancelar", fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = onSaveClick,
                modifier = Modifier.weight(1f).height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MagentaPink)
            ) {
                Text("Guardar", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    } else {
        Button(
            onClick = onEditClick,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = TealDark)
        ) {
            Text("Editar", fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDropdownField(
    label: String,
    selectedOption: String,
    options: List<String>,
    isEditing: Boolean,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
        Text(text = label, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkGrayText)
        Spacer(modifier = Modifier.height(4.dp))

        ExposedDropdownMenuBox(
            expanded = isEditing && expanded,
            onExpandedChange = { if (isEditing) expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedOption,
                onValueChange = {},
                readOnly = true,
                enabled = isEditing,
                trailingIcon = { if (isEditing) ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.fillMaxWidth().menuAnchor(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TealDark,
                    unfocusedBorderColor = LightBlueGray,
                    focusedTextColor = DarkGrayText,
                    unfocusedTextColor = DarkGrayText,
                    disabledBorderColor = Color.Transparent,
                    disabledTextColor = DarkGrayText,
                    disabledContainerColor = Color.Transparent,
                    disabledTrailingIconColor = Color.Transparent
                )
            )
            if (isEditing) {
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option, color = DarkGrayText) },
                            onClick = {
                                onOptionSelected(option)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@SuppressLint("NonObservableLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDateField(
    label: String,
    fechaMilisegundos: Long,
    isEditing: Boolean,
    onDateSelected: (Long) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    val fechaFormateada = if (fechaMilisegundos > 0L) sdf.format(Date(fechaMilisegundos)) else "Seleccionar fecha"

    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
        Text(text = label, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkGrayText)
        Spacer(modifier = Modifier.height(4.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = fechaFormateada,
                onValueChange = {},
                readOnly = true,
                enabled = isEditing,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TealDark,
                    unfocusedBorderColor = LightBlueGray,
                    focusedTextColor = DarkGrayText,
                    unfocusedTextColor = DarkGrayText,
                    disabledBorderColor = Color.Transparent,
                    disabledTextColor = DarkGrayText,
                    disabledContainerColor = Color.Transparent
                )
            )
            if (isEditing) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable { showDialog = true }
                        .background(Color.Transparent)
                )
            }
        }
    }

    if (showDialog && isEditing) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = if (fechaMilisegundos > 0L) fechaMilisegundos else System.currentTimeMillis()
        )

        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { onDateSelected(it) }
                    showDialog = false
                }) {
                    Text("Aceptar", color = TealDark, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancelar", color = LightBlueGray)
                }
            },
            colors = DatePickerDefaults.colors(containerColor = Color.White)
        ) {
            DatePicker(state = datePickerState)
        }
    }
}



@Composable
fun ProfileAvatarSection(
    nombre: String,
    correo: String,
    fotoPerfil: String?,
    onEditPhotoClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            if (fotoPerfil.isNullOrEmpty()) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Avatar del usuario",
                    tint = Color.White.copy(alpha = 0.9f),
                    modifier = Modifier.size(100.dp)
                )
            } else {
                AsyncImage(
                    model = fotoPerfil,
                    contentDescription = "Avatar del usuario",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.White, CircleShape)
                )
            }

            IconButton(
                onClick = onEditPhotoClick,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = (-4).dp, y = (-4).dp)
                    .size(20.dp)
                    .background(TealDark, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Cambiar foto",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = nombre, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text(text = correo, fontSize = 14.sp, color = Color.White.copy(alpha = 0.8f))
    }
}

@Composable
fun PhotoPreviewDialog(
    imageUri: Uri,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Vista Previa", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = DarkGrayText)
                Spacer(modifier = Modifier.height(16.dp))

                AsyncImage(
                    model = imageUri,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .border(4.dp, TealLight, CircleShape)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    TextButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                        Text("Cancelar", color = LightBlueGray)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = onSave,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = TealDark)
                    ) {
                        Text("Guardar", color = Color.White)
                    }
                }
            }
        }
    }
}