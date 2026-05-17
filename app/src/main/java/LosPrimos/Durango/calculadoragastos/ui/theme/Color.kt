package LosPrimos.Durango.calculadoragastos.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Main paleta SPENT
val TealDark = Color(0xFF1C847B)
val TealLight = Color(0xFF5AD6CA)
val MagentaPink = Color(0xFF13B2A1)
val DarkGrayText = Color(0xFF1F2933)
val LightBlueGray = Color(0xFF9AA6AC)

// Colores de estructura

val ViviendaColor        = Color(0xFF5C6BC0)
val EntretenimientoColor = Color(0xFFAB47BC)
val BackgroundLight = Color(0xFFF3F5F2)
val SurfaceWhite = Color(0xFFFFFFFF)
val BackgroundDark = Color(0xFF121212)
val SurfaceDark = Color(0xFF1E1E1E)
val ErrorRed = Color(0xFFD32F2F)

val YellowGraph = Color(0xFFFFC107)
val GreenGraph = Color(0xFF4CAF50)

// Gradiente del fondo
//val MainGradient = Brush.linearGradient(
//    0.45f to TealDark,
//    0.63f to TealLight,
//    0.80f to MagentaPink
//)
val MainGradient = Brush.horizontalGradient(
    colors = listOf(
        TealDark,
        MagentaPink

    )
)
