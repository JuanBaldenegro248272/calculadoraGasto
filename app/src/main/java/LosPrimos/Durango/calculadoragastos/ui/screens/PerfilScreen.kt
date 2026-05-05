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

@Composable
fun PerfilScreen(
    onNavigate: (String) -> Unit,
    onLogoutClick: () -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    val isOffline = true

    var nombre by remember { mutableStateOf("Héctor Garduño") }
    val correo = "elZombie32@cartablanca.com"
    var fechaNacimiento by remember { mutableStateOf("29/11/2005") }
    var genero by remember { mutableStateOf("Masculino") }

    var isDarkMode by remember { mutableStateOf(false) }
    var isBiometricEnabled by remember { mutableStateOf(true) }

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

                    ProfileAvatarSection(nombre = nombre, correo = correo)

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
                                onCancelClick = { isEditing = false },
                                onSaveClick = {
                                    isEditing = false
                                }
                            )

                            ProfileFieldItem(
                                label = "Nombre Completo",
                                value = nombre,
                                isEditing = isEditing,
                                onValueChange = { nombre = it }
                            )

                            ProfileFieldItem(
                                label = "Correo Electrónico",
                                value = correo,
                                isEditing = false,
                                onValueChange = {}
                            )

                            ProfileFieldItem(
                                label = "Fecha de Nacimiento",
                                value = fechaNacimiento,
                                isEditing = isEditing,
                                onValueChange = { fechaNacimiento = it }
                            )

                            ProfileFieldItem(
                                label = "Género",
                                value = genero,
                                isEditing = isEditing,
                                onValueChange = { genero = it }
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            ProfileSectionTitle("Configuración")

                            ProfileSwitchItem(
                                title = "Tema Oscuro",
                                icon = Icons.Default.NightsStay,
                                isChecked = isDarkMode,
                                onCheckedChange = { isDarkMode = it }
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