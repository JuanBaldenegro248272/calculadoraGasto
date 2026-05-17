package LosPrimos.Durango.calculadoragastos.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import LosPrimos.Durango.calculadoragastos.data.entities.GastoGrupo
import LosPrimos.Durango.calculadoragastos.ui.components.*
import LosPrimos.Durango.calculadoragastos.ui.theme.*
import LosPrimos.Durango.calculadoragastos.utils.SwipeToReveal
import LosPrimos.Durango.calculadoragastos.viewModel.GastoGrupoViewModel
import LosPrimos.Durango.calculadoragastos.viewModel.GrupoViewModel
import java.time.Instant
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetalleGrupoScreen(
    idGrupo: String,
    onNavigate: (String) -> Unit,
    onBack: () -> Unit,
    grupoViewModel: GrupoViewModel,
    gastoGrupoViewModel: GastoGrupoViewModel
) {
    val grupo = grupoViewModel.grupos.find { it.idGrupo == idGrupo }
    val gastos = gastoGrupoViewModel.gastosGrupo
    val nombresUsuarios by gastoGrupoViewModel.nombresUsuarios.collectAsState()
    val usuarioActualId by gastoGrupoViewModel.usuarioActualId.collectAsState()
    val numMiembros = grupo?.miembrosIds?.size?.takeIf { it > 0 } ?: 1

    val mesesMapReverso = mapOf(
        1 to "Enero", 2 to "Febrero", 3 to "Marzo", 4 to "Abril",
        5 to "Mayo", 6 to "Junio", 7 to "Julio", 8 to "Agosto",
        9 to "Septiembre", 10 to "Octubre", 11 to "Noviembre", 12 to "Diciembre"
    )
    val mesesMap = mesesMapReverso.entries.associate { (k, v) -> v to k }
    val mesActualTexto = remember {
        mesesMapReverso[java.time.LocalDate.now().monthValue] ?: "Todos"
    }
    var mesSeleccionado by remember { mutableStateOf(mesActualTexto) }

    var gastoSeleccionado by remember { mutableStateOf<GastoGrupo?>(null) }

    LaunchedEffect(idGrupo) {
        gastoGrupoViewModel.obtenerGastos(idGrupo)
    }

    val gastosFiltrados = gastos.filter { gasto ->
        if (mesSeleccionado == "Todos") true
        else {
            val date = Instant.ofEpochMilli(gasto.fecha).atZone(ZoneId.systemDefault()).toLocalDate()
            date.monthValue == mesesMap[mesSeleccionado]
        }
    }

    val totalGastado = gastosFiltrados.sumOf { it.montoTotal }
    val tuParte = if (numMiembros > 0) totalGastado / numMiembros else 0.0

    Scaffold(
        bottomBar = {
            SpentBottomNavigation(
                currentRoute = "grupos",
                onNavigate = { onNavigate(it) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onNavigate("agregarGastoGrupo/$idGrupo") },
                containerColor = TealDark,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar gasto al grupo")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MainGradient)
                .padding(paddingValues)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar", tint = Color.White)
                    }
                    Text(
                        text = grupo?.nombre ?: "Grupo",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                if (!grupo?.imagenGrupo.isNullOrBlank()) {
                    AsyncImage(
                        model = grupo!!.imagenGrupo,
                        contentDescription = "Imagen del grupo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .padding(horizontal = 16.dp)
                            .clip(RoundedCornerShape(16.dp))
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    GrupoMiniCard(
                        titulo = "Total",
                        monto = totalGastado,
                        icono = Icons.Default.AccountBalanceWallet,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    GrupoMiniCard(
                        titulo = "Tu parte",
                        monto = tuParte,
                        icono = Icons.Default.Person,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    GrupoMiniCard(
                        titulo = "Miembros",
                        monto = numMiembros.toDouble(),
                        icono = Icons.Default.Group,
                        esMonto = false,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                BottomRoundedSurface {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {

                        // Filtro por mes
                        item {
                            GrupoFiltroMes(
                                mesSeleccionado = mesSeleccionado,
                                onMesSeleccionado = { mesSeleccionado = it }
                            )
                        }

                        if (gastosFiltrados.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(32.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "No hay gastos en este mes",
                                        color = LightBlueGray,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        } else {
                            item {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Receipt,
                                        contentDescription = null,
                                        tint = DarkGrayText,
                                        modifier = Modifier.size(22.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "GASTOS DEL GRUPO",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 14.sp,
                                        color = DarkGrayText,
                                        letterSpacing = 1.sp
                                    )
                                }
                                Divider(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    color = LightBlueGray.copy(alpha = 0.5f)
                                )
                            }

                            gastosFiltrados.forEach { gasto ->
                                item(key = gasto.idGastoGrupo) {
                                    val esMiGasto = gasto.idUsuarioPagador == usuarioActualId
                                    val tuParteItem = gasto.montoTotal / numMiembros

                                    if (esMiGasto) {
                                        SwipeToReveal(
                                            onEdit = {
                                                onNavigate("editarGastoGrupo/$idGrupo/${gasto.idGastoGrupo}")
                                            },
                                            onDelete = {
                                                gastoGrupoViewModel.eliminarGasto(gasto)
                                            }
                                        ) {
                                            Box(modifier = Modifier.clickable { gastoSeleccionado = gasto }) {
                                                GrupoTransactionItem(
                                                    descripcion = gasto.descripcion,
                                                    fecha = formatoFecha(gasto.fecha),
                                                    montoTotal = gasto.montoTotal,
                                                    nombreCreador = nombresUsuarios[gasto.idUsuarioPagador] ?: "...",
                                                    tuParte = tuParteItem,
                                                    esMiGasto = true
                                                )
                                            }
                                        }
                                    } else {
                                        Box(modifier = Modifier.clickable { gastoSeleccionado = gasto }) {
                                            GrupoTransactionItem(
                                                descripcion = gasto.descripcion,
                                                fecha = formatoFecha(gasto.fecha),
                                                montoTotal = gasto.montoTotal,
                                                nombreCreador = nombresUsuarios[gasto.idUsuarioPagador] ?: "...",
                                                tuParte = tuParteItem,
                                                esMiGasto = false
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
    }
}

@Composable
private fun GrupoMiniCard(
    titulo: String,
    monto: Double,
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    esMonto: Boolean = true
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite.copy(alpha = 0.4f)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icono, contentDescription = null, tint = SurfaceWhite, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(titulo, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = SurfaceWhite)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = if (esMonto) "$${"%.2f".format(monto)}" else monto.toInt().toString(),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 13.sp,
                color = SurfaceWhite
            )
        }
    }
}

@Composable
private fun GrupoFiltroMes(
    mesSeleccionado: String,
    onMesSeleccionado: (String) -> Unit
) {
    val meses = listOf(
        "Todos", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
        "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    )
    var expandido by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Mes:", fontWeight = FontWeight.Bold, color = DarkGrayText, fontSize = 14.sp)
        Spacer(modifier = Modifier.width(8.dp))
        Box {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { expandido = true }
            ) {
                Text(mesSeleccionado, color = TealDark, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = TealDark)
            }
            DropdownMenu(
                expanded = expandido,
                onDismissRequest = { expandido = false },
                modifier = Modifier.background(SurfaceWhite)
            ) {
                meses.forEach { mes ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = mes,
                                color = if (mes == mesSeleccionado) TealDark else DarkGrayText,
                                fontWeight = if (mes == mesSeleccionado) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        onClick = { onMesSeleccionado(mes); expandido = false }
                    )
                }
            }
        }
    }
}

@Composable
fun GrupoTransactionItem(
    descripcion: String,
    fecha: String,
    montoTotal: Double,
    nombreCreador: String,
    tuParte: Double,
    esMiGasto: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(descripcion, fontWeight = FontWeight.Bold, color = DarkGrayText, fontSize = 16.sp)
            Text(fecha, color = LightBlueGray, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = if (esMiGasto) "Tú" else nombreCreador,
                color = if (esMiGasto) TealDark else LightBlueGray,
                fontSize = 12.sp,
                fontWeight = if (esMiGasto) FontWeight.Bold else FontWeight.Normal
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "-$${"%.2f".format(montoTotal)}",
                fontWeight = FontWeight.ExtraBold,
                color = MagentaPink,
                fontSize = 16.sp
            )
            Text(
                text = "Tu parte: $${"%.2f".format(tuParte)}",
                color = DarkGrayText,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}