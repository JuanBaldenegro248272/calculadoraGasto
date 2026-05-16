package LosPrimos.Durango.calculadoragastos.ui.screens

import LosPrimos.Durango.calculadoragastos.data.entities.Grupo
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import LosPrimos.Durango.calculadoragastos.ui.theme.MainGradient
import LosPrimos.Durango.calculadoragastos.ui.components.*
import LosPrimos.Durango.calculadoragastos.viewModel.GrupoViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun GruposScreen(
    onNavigate: (String) -> Unit,
    grupoViewModel: GrupoViewModel
) {
    val usuarioId by grupoViewModel.usuarioActualId.collectAsState()
    val isOffline = true
    var showJoinDialog by remember { mutableStateOf(false) }
    var showCreateDialog by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            SpentBottomNavigation(
                currentRoute = "grupos",
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

            if (showJoinDialog) {
                JoinGroupDialog(
                    onDismiss = { showJoinDialog = false },
                    onJoinConfirm = { codigoIngresado ->
                        println("El usuario intentó unirse con el código: $codigoIngresado")
                        showJoinDialog = false
                    }
                )
            }


            if (showCreateDialog) {
                CreateGroupDialog(
                    onDismiss = { showCreateDialog = false },
                    onCreateConfirm = { nombre, categoria, codigo ->
                        val nuevoGrupo = Grupo(
                            nombre = nombre,
                            tipo = categoria,
                            imagenGrupo = "",
                            codigo = codigo,
                            idUsuario = usuarioId,
                            miembros = listOf(usuarioId)
                        )
                        grupoViewModel.crearGrupo(nuevoGrupo)
                        showCreateDialog = false
                    }
                )
            }


            Column(modifier = Modifier.fillMaxSize()) {

                if (isOffline) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        OfflineStatusBar()
                    }
                }

                GroupsTopBar()

                GroupActionButtons(
                    onCreateClick = { showCreateDialog = true },
                    onJoinClick = { showJoinDialog = true }
                )

                Spacer(modifier = Modifier.height(24.dp))

                BottomRoundedSurface {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp)
                    ) {
                        item {
                            GroupCardItem(
                                titulo = "Viaje a Cancún",
                                categoriaGrupo = "Amigos",
                                cantidadMiembros = 6,
                                montoTotal = 12000.0,
                                codigoGrupo = "X7B9K",
                                onClick = { onNavigate("detalleGrupo/1") }
                            )
                        }

                        item {
                            GroupCardItem(
                                titulo = "Roomies Depa",
                                categoriaGrupo = "Compañeros",
                                cantidadMiembros = 3,
                                montoTotal = 4500.0,
                                codigoGrupo = "DEP24",
                                onClick = { onNavigate("detalleGrupo/2") }
                            )
                        }

                        item {
                            GroupCardItem(
                                titulo = "Cena Fin de Año",
                                categoriaGrupo = "Familia",
                                cantidadMiembros = 12,
                                montoTotal = 8500.0,
                                codigoGrupo = "CEN12",
                                onClick = { onNavigate("detalleGrupo/3") }
                            )
                        }
                    }
                }
            }
        }
    }
}