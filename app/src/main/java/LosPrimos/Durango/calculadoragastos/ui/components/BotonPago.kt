package LosPrimos.Durango.calculadoragastos.ui.components

import LosPrimos.Durango.calculadoragastos.ui.theme.TealDark
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BotonPago(
    texto: String,
    seleccionado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(40.dp),
        shape = RoundedCornerShape(9.dp),
        border = if (seleccionado) null else BorderStroke(1.dp, Color(0xFFC8C2D2)),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (seleccionado) TealDark else Color(0xFFF3F3F3),
            contentColor = if (seleccionado) Color.White else Color(0xFF353541)
        )
    ) {
        Text(text = texto, fontSize = 14.sp)
    }
}