package LosPrimos.Durango.calculadoragastos.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// config modo oscurro
private val DarkColorScheme = darkColorScheme(
    primary = TealLight,
    secondary = MagentaPink,
    background = BackgroundDark,
    surface = SurfaceDark,
    onPrimary = Color.White,
    onBackground = LightBlueGray,
    onSurface = LightBlueGray,
    error = ErrorRed
)

// config modo claro
private val LightColorScheme = lightColorScheme(
    primary = TealDark,
    secondary = MagentaPink,
    background = BackgroundLight,
    surface = SurfaceWhite,
    onPrimary = Color.White,
    onBackground = DarkGrayText,
    onSurface = DarkGrayText,
    error = ErrorRed
)

@Composable
fun CalculadoraGastosTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}