package LosPrimos.Durango.calculadoragastos.ui.components

import LosPrimos.Durango.calculadoragastos.data.entities.Gasto
import LosPrimos.Durango.calculadoragastos.data.enums.TipoPago
import LosPrimos.Durango.calculadoragastos.ui.screens.formatoFecha
import LosPrimos.Durango.calculadoragastos.ui.theme.MagentaPink
import LosPrimos.Durango.calculadoragastos.ui.theme.TealDark
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import java.text.NumberFormat

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetalleGastoDialog(
    gasto: Gasto,
    categoriaNombre: String,
    onDismiss: () -> Unit
) {
    val formatoDinero = NumberFormat.getCurrencyInstance()
    var mostrarImagenCompleta by remember {
        mutableStateOf(false)
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Detalle del Gasto",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TealDark
                )
                Divider(color = Color(0xFFF0F0F0))

                DetalleFila(etiqueta = "Categoría:", valor = categoriaNombre)
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Descripción:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    Text(
                        text = gasto.descripcion,
                        fontSize = 15.sp,
                        color = Color.Black,
                        lineHeight = 22.sp
                    )
                }
                DetalleFila(
                    etiqueta = "Monto:",
                    valor = formatoDinero.format(gasto.monto),
                    isMonto = true,
                    isGasto = true
                )
                DetalleFila(etiqueta = "Fecha:", valor = formatoFecha(gasto.fecha))
                DetalleFila(etiqueta = "Método:", valor = if (gasto.tipoPago == TipoPago.EFECTIVO) "Efectivo" else "Tarjeta")

                if (!gasto.lugar.isNullOrEmpty()) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Ubicacion:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )

                        Text(
                            text = gasto.lugar,
                            fontSize = 15.sp,
                            color = Color.Black,
                            lineHeight = 22.sp
                        )
                    }
                }

                if (!gasto.fotoRecibo.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Comprobante:", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Gray)
                    AsyncImage(
                        model = Uri.parse(gasto.fotoRecibo),
                        contentDescription = "Comprobante de gasto",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(
                                1.dp,
                                Color(0xFFE0E0E0),
                                RoundedCornerShape(12.dp)
                            )
                            .clickable {
                                mostrarImagenCompleta = true
                            }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MagentaPink),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cerrar", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    if (mostrarImagenCompleta) {

        var scale by remember { mutableFloatStateOf(1f) }
        var offsetX by remember { mutableFloatStateOf(0f) }
        var offsetY by remember { mutableFloatStateOf(0f) }

        Dialog(
            onDismissRequest = {
                mostrarImagenCompleta = false
            }
        ) {

            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Black.copy(alpha = 0.82f))
                    .padding(15.dp),
                contentAlignment = Alignment.Center
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .wrapContentHeight()
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color.White)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {}
                        .padding(12.dp)
                ) {

                    AsyncImage(
                        model = Uri.parse(gasto.fotoRecibo),
                        contentDescription = "Comprobante ampliado",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                                translationX = offsetX
                                translationY = offsetY
                            }
                            .pointerInput(Unit) {

                                detectTransformGestures { _, pan, zoom, _ ->

                                    scale = (scale * zoom)
                                        .coerceIn(1f, 5f)

                                    offsetX += pan.x
                                    offsetY += pan.y
                                }
                            }
                    )

                    IconButton(
                        onClick = {
                            mostrarImagenCompleta = false
                        },
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = Color.Black
                        )
                    }
                }
            }
        }
    }
}