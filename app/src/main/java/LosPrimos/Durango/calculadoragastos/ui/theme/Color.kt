package LosPrimos.Durango.calculadoragastos.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Main paleta SPENT
val TealDark = Color(0xFF137A7F)
val TealLight = Color(0xFF86CECB)
val MagentaPink = Color(0xFFE12885)
val DarkGrayText = Color(0xFF373B3E)
val LightBlueGray = Color(0xFFBEC8D1)

// Colores de estructura
val BackgroundLight = Color(0xFFF5F7F9)
val SurfaceWhite = Color(0xFFFFFFFF)
val BackgroundDark = Color(0xFF121212)
val SurfaceDark = Color(0xFF1E1E1E)
val ErrorRed = Color(0xFFD32F2F)

// Gradiente del fondo
//val MainGradient = Brush.linearGradient(
//    0.45f to TealDark,
//    0.63f to TealLight,
//    0.80f to MagentaPink
//)
val MainGradient = Brush.horizontalGradient(
    colors = listOf(
        TealDark,
        TealLight,
        TealLight,
        MagentaPink

    )
)