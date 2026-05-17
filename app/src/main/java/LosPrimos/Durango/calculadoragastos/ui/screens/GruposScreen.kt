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
import LosPrimos.Durango.calculadoragastos.utils.NetworkConnectivityObserver
import LosPrimos.Durango.calculadoragastos.viewModel.GrupoViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun GruposScreen(
    onNavigate: (String) -> Unit,
    grupoViewModel: GrupoViewModel
) {
    val usuarioId by grupoViewModel.usuarioActualId.collectAsState()
    val isOffline = true
    var showJoinDialog by remember { mutableStateOf(false) }
    var showCreateDialog by remember { mutableStateOf(false) }
    val grupos = grupoViewModel.grupos

    val context = LocalContext.current

    val connectivityObserver = remember { NetworkConnectivityObserver(context) }

    val hasInternet by connectivityObserver.observe().collectAsState(initial = true)


    LaunchedEffect(usuarioId) {
        if (usuarioId != 0){
            grupoViewModel.obtenerGrupos(usuarioId)
        }
    }
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

            if (!hasInternet) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.CloudOff,
                        contentDescription = "Sin conexión",
                        tint = Color.White,
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Sin conexión a Internet",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Text(
                        "La sección de grupos requiere conexión.",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )
                }
            } else {
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
                        grupos.forEach { grupo ->
                            item {
                                GroupCardItem(
                                    titulo = grupo.nombre,
                                    categoriaGrupo = grupo.tipo,
                                    cantidadMiembros = grupo.miembros.size,
                                    montoTotal = 0.0,
                                    codigoGrupo = grupo.codigo,
                                    onClick = { onNavigate("detalleGrupo/1") }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
    }