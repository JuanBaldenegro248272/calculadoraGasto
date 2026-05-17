package LosPrimos.Durango.calculadoragastos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import LosPrimos.Durango.calculadoragastos.ui.theme.*
import LosPrimos.Durango.calculadoragastos.ui.components.*
import LosPrimos.Durango.calculadoragastos.utils.saveImageToInternalStorage
import LosPrimos.Durango.calculadoragastos.viewModel.PerfilViewModel
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext

@Composable
fun PerfilScreen(
    onNavigate: (String) -> Unit,
    onLogoutClick: () -> Unit,
    viewModel: PerfilViewModel
) {
    val usuario by viewModel.usuarioState.collectAsState()

    var isEditing by remember { mutableStateOf(false) }
    val isOffline = true

    var nombreEdit by remember { mutableStateOf("") }
    var generoEdit by remember { mutableStateOf("") }
    var fechaEdit by remember { mutableStateOf(0L) }

    var isBiometricEnabled by remember { mutableStateOf(true) }

    val isDarkMode by viewModel.isDarkMode.collectAsState()

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    val context = LocalContext.current

    if (selectedImageUri != null) {
        PhotoPreviewDialog(
            imageUri = selectedImageUri!!,
            onDismiss = { selectedImageUri = null },
            onSave = {
                val localUri = saveImageToInternalStorage(context, selectedImageUri!!)

                if (localUri != null) {
                    viewModel.actualizarFotoPerfil(localUri.toString())
                }

                selectedImageUri = null
            }
        )
    }

    LaunchedEffect(usuario) {
        usuario?.let {
            nombreEdit = it.nombre
            generoEdit = it.genero
            fechaEdit = it.fechaNacimiento
        }
    }

    Scaffold(
        bottomBar = {
            SpentBottomNavigation(
                currentRoute = "perfil",
                onNavigate = { onNavigate(it) }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MainGradient)
                .padding(paddingValues)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {

                Column {
                    if (isOffline) {
                        OfflineStatusBar()
                    }

                    ProfileTopBar(onBackClick = { onNavigate("home") })

                    ProfileAvatarSection(
                        nombre = usuario?.nombre ?: "Cargando...",
                        correo = usuario?.correo ?: "",
                        fotoPerfil = usuario?.fotoPerfil,
                        onEditPhotoClick = {
                            galleryLauncher.launch("image/*")
                        }
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }

                BottomRoundedSurface {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 24.dp),
                        contentPadding = PaddingValues(bottom = 32.dp)
                    ) {
                        item {
                            ProfileSectionHeader(
                                title = "Información personal",
                                isEditing = isEditing,
                                onEditClick = { isEditing = true },
                                onCancelClick = {
                                    isEditing = false
                                    usuario?.let {
                                        nombreEdit = it.nombre
                                        generoEdit = it.genero
                                    }
                                },
                                onSaveClick = {
                                    viewModel.actualizarPerfil(nombreEdit, generoEdit, fechaEdit)
                                    isEditing = false
                                }
                            )

                            ProfileFieldItem(
                                label = "Nombre Completo",
                                value = nombreEdit,
                                isEditing = isEditing,
                                onValueChange = { nombreEdit = it }
                            )

                            ProfileFieldItem(
                                label = "Correo Electrónico",
                                value = usuario?.correo ?: "",
                                isEditing = false,
                                onValueChange = {}
                            )

                            ProfileDateField(
                                label = "Fecha de Nacimiento",
                                fechaMilisegundos = fechaEdit,
                                isEditing = isEditing,
                                onDateSelected = { nuevaFecha -> fechaEdit = nuevaFecha }
                            )

                            ProfileDropdownField(
                                label = "Género",
                                selectedOption = generoEdit,
                                options = listOf("Masculino", "Femenino", "Otro"),
                                isEditing = isEditing,
                                onOptionSelected = { nuevaOpcion -> generoEdit = nuevaOpcion }
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            ProfileSectionTitle("Configuración")

                            ProfileSwitchItem(
                                title = "Tema Oscuro",
                                icon = Icons.Default.NightsStay,
                                isChecked = isDarkMode,
                                onCheckedChange = { viewModel.toggleDarkMode(it) }
                            )

                            ProfileSwitchItem(
                                title = "Autenticación biométrica",
                                icon = Icons.Default.Fingerprint,
                                isChecked = isBiometricEnabled,
                                onCheckedChange = { isBiometricEnabled = it }
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(16.dp))

                            ProfileLogoutButton(onClick = onLogoutClick)

                            Spacer(modifier = Modifier.height(24.dp))


                        }
                    }
                }
            }
        }
    }
}