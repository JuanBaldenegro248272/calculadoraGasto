package LosPrimos.Durango.calculadoragastos.ui.screens

import LosPrimos.Durango.calculadoragastos.ui.components.SpentBottomNavigation
import LosPrimos.Durango.calculadoragastos.data.entities.GastoGrupo
import LosPrimos.Durango.calculadoragastos.ui.theme.BackgroundLight
import LosPrimos.Durango.calculadoragastos.ui.theme.DarkGrayText
import LosPrimos.Durango.calculadoragastos.ui.theme.LightBlueGray
import LosPrimos.Durango.calculadoragastos.ui.theme.MagentaPink
import LosPrimos.Durango.calculadoragastos.ui.theme.MainGradient
import LosPrimos.Durango.calculadoragastos.ui.theme.TealDark
import LosPrimos.Durango.calculadoragastos.ui.theme.SurfaceWhite
import LosPrimos.Durango.calculadoragastos.viewModel.GastoGrupoViewModel
import LosPrimos.Durango.calculadoragastos.viewModel.GrupoViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.South
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun DetalleGrupoScreen(
    grupoId: String,
    grupoViewModel: GrupoViewModel,
    gastoGrupoViewModel: GastoGrupoViewModel,
    onBack: () -> Unit,
    onNavigate: (String) -> Unit
) {
    val grupo = grupoViewModel.grupoSeleccionado
    val gastosGrupo = gastoGrupoViewModel.gastosGrupo
    val totalSalud = gastosGrupo.filter { it.idCategoria == 5 }.sumOf { it.montoTotal }
    val totalAlimentacion = gastosGrupo.filter { it.idCategoria == 4 }.sumOf { it.montoTotal }
    val totalTransporte = gastosGrupo.filter { it.idCategoria == 3 }.sumOf { it.montoTotal }
    val totalEntretenimiento = gastosGrupo.filter { it.idCategoria == 2 }.sumOf { it.montoTotal }
    val usuarioActualId = gastoGrupoViewModel.usuarioActualId.value

    LaunchedEffect(grupoId) {
        grupoViewModel.obtenerGrupoPorId(grupoId)
        gastoGrupoViewModel.obtenerGastos(grupoId)
    }

    Scaffold(
        bottomBar = {
            SpentBottomNavigation(
                currentRoute = "grupos",
                onNavigate = onNavigate
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onNavigate("agregarGastoGrupo/$grupoId") },
                containerColor = TealDark,
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar gasto"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SurfaceWhite)
                .padding(paddingValues)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(88.dp)
                    .background(MainGradient)
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Regresar",
                        tint = Color.White
                    )
                }

                Text(
                    text = grupo?.nombre ?: "Grupo",
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 25.sp,
                    lineHeight = 28.sp
                )
            }

            if (grupo == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Cargando grupo...",
                        color = DarkGrayText,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp, vertical = 26.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ImagenGrupo(
                        imagenGrupo = grupo.imagenGrupo,
                        nombreGrupo = grupo.nombre
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = null,
                            tint = DarkGrayText,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Codigo: ${grupo.codigo}",
                            color = DarkGrayText,
                            fontSize = 11.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 18.dp)
                        ) {
                            CategoriaDetalleGrupo(
                                nombre = "Salud",
                                monto = totalSalud,
                                icono = Icons.Default.LocalHospital,
                                gastos = gastosGrupo.filter { it.idCategoria == 5 },
                                usuarioActualId = usuarioActualId,
                                onEliminar = { gastoGrupoViewModel.eliminarGasto(it) }
                            )
                            CategoriaDetalleGrupo(
                                nombre = "Alimentacion",
                                monto = totalAlimentacion,
                                icono = Icons.Default.Restaurant,
                                gastos = gastosGrupo.filter { it.idCategoria == 4 },
                                usuarioActualId = usuarioActualId,
                                onEliminar = { gastoGrupoViewModel.eliminarGasto(it) }
                            )
                            CategoriaDetalleGrupo(
                                nombre = "Transporte",
                                monto = totalTransporte,
                                icono = Icons.Default.DirectionsCar,
                                gastos = gastosGrupo.filter { it.idCategoria == 3 },
                                usuarioActualId = usuarioActualId,
                                onEliminar = { gastoGrupoViewModel.eliminarGasto(it) }
                            )
                            CategoriaDetalleGrupo(
                                nombre = "Entretenimiento",
                                monto = totalEntretenimiento,
                                icono = Icons.Default.Celebration,
                                gastos = gastosGrupo.filter { it.idCategoria == 2 },
                                usuarioActualId = usuarioActualId,
                                onEliminar = { gastoGrupoViewModel.eliminarGasto(it) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ImagenGrupo(
    imagenGrupo: String,
    nombreGrupo: String
) {
    if (imagenGrupo.isNotBlank()) {
        AsyncImage(
            model = imagenGrupo,
            contentDescription = nombreGrupo,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(150.dp)
                .height(96.dp)
        )
    } else {
        Box(
            modifier = Modifier
                .width(150.dp)
                .height(96.dp)
                .background(BackgroundLight, RoundedCornerShape(2.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Group,
                contentDescription = null,
                tint = TealDark,
                modifier = Modifier.size(42.dp)
            )
        }
    }
}

@Composable
private fun CategoriaDetalleGrupo(
    nombre: String,
    monto: Double,
    icono: ImageVector,
    gastos: List<GastoGrupo>,
    usuarioActualId: String?,
    onEliminar: (GastoGrupo) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 9.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(25.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = nombre,
                color = Color.Black,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "$${"%.2f".format(monto)}",
                color = if (monto > 0.0) Color.Red else MagentaPink,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
        }

        gastos.forEach { gasto ->
            GastoGrupoItem(
                gasto = gasto,
                esMio = gasto.idUsuarioPagador == usuarioActualId,
                onEliminar = { onEliminar(gasto) }
            )
        }
    }
}

@Composable
private fun GastoGrupoItem(
    gasto: GastoGrupo,
    esMio: Boolean,
    onEliminar: () -> Unit
) {
    val cantidadMiembros = if (gasto.cantidadMiembros <= 0) 1 else gasto.cantidadMiembros
    val miParte = gasto.montoTotal / cantidadMiembros

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 38.dp, top = 6.dp, bottom = 6.dp)
            .background(BackgroundLight, RoundedCornerShape(10.dp))
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.South,
            contentDescription = null,
            tint = Color.Red,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = gasto.descripcion,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = if (esMio) "Tu" else "Otro",
                    color = Color.Black,
                    fontSize = 11.sp
                )
            }

            Text(
                text = "- $${"%.2f".format(gasto.montoTotal)}",
                color = Color.Red,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )

            Text(
                text = "Tu parte: $${"%.2f".format(miParte)}",
                color = Color.Black,
                fontSize = 11.sp
            )
        }

        IconButton(onClick = onEliminar) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Eliminar gasto",
                tint = Color.Black,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
