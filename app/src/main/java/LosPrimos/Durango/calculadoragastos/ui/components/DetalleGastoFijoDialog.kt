package LosPrimos.Durango.calculadoragastos.ui.components

import LosPrimos.Durango.calculadoragastos.data.entities.Gasto
import LosPrimos.Durango.calculadoragastos.ui.theme.MagentaPink
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import java.text.NumberFormat

@Composable
fun DetalleGastoFijoDialog(
    gasto: Gasto,
    onDismiss: () -> Unit
) {
    val formatoDinero = NumberFormat.getCurrencyInstance()

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
                    text = "Detalle del Gasto Fijo",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MagentaPink
                )
                Divider(color = Color(0xFFF0F0F0))

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Nombre:", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Gray)
                    Text(text = gasto.descripcion, fontSize = 15.sp, color = Color.Black, lineHeight = 22.sp)
                }

                DetalleFila(
                    etiqueta = "Monto:",
                    valor = formatoDinero.format(gasto.monto),
                    isMonto = true,
                    isGasto = true
                )

                if (!gasto.lugar.isNullOrEmpty()) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Frecuencia:", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Gray)
                        Text(text = gasto.lugar, fontSize = 15.sp, color = Color.Black, lineHeight = 22.sp)
                    }
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
}