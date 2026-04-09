package LosPrimos.Durango.calculadoragastos.ui.components

import android.view.RoundedCorner
import android.widget.ProgressBar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ProgressBar(gastado: Double, balance: Double){

    var porcentaje = if (balance > 0) (gastado/balance).toFloat() else 0f

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(12.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White.copy(alpha = 0.5f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(porcentaje)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF0047FF))
        )
    }
}

@Composable
@Preview
fun ProgressBarPreview(){
    ProgressBar(300.00, 1500.00)
}