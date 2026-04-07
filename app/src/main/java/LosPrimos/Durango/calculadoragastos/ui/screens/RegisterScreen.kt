package LosPrimos.Durango.calculadoragastos.ui.screens

import LosPrimos.Durango.calculadoragastos.ui.theme.AzulApp
import LosPrimos.Durango.calculadoragastos.ui.theme.AzulGenero
import LosPrimos.Durango.calculadoragastos.ui.theme.ColorLabel
import LosPrimos.Durango.calculadoragastos.ui.theme.GrisBorde
import LosPrimos.Durango.calculadoragastos.ui.theme.GrisPlaceholder
import LosPrimos.Durango.calculadoragastos.ui.theme.RosaGenero
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun RegisterScreen(
    onCreateAccountClick: () -> Unit = {},
    onLoginClick: () -> Unit = {}
) {
    var nombre      by remember { mutableStateOf("") }
    var email       by remember { mutableStateOf("") }
    var dd          by remember { mutableStateOf("") }
    var mm          by remember { mutableStateOf("") }
    var yyyy        by remember { mutableStateOf("") }
    var genero      by remember { mutableStateOf<String?>(null) }
    var password    by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.Start
    ) {


        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Crear Cuenta",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(28.dp))


        FieldLabel("Nombre:")
        RegisterTextField(
            value = nombre,
            onValueChange = { nombre = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        Spacer(modifier = Modifier.height(16.dp))


        FieldLabel("Correo Electronico:")
        RegisterTextField(
            value = email,
            onValueChange = { email = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(16.dp))


        FieldLabel("Fecha de Nacimiento:")
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DatePartField(
                value = dd,
                onValueChange = { if (it.length <= 2) dd = it },
                placeholder = "DD",
                modifier = Modifier.width(72.dp)
            )
            DatePartField(
                value = mm,
                onValueChange = { if (it.length <= 2) mm = it },
                placeholder = "MM",
                modifier = Modifier.width(72.dp)
            )
            DatePartField(
                value = yyyy,
                onValueChange = { if (it.length <= 4) yyyy = it },
                placeholder = "YYYY",
                modifier = Modifier.width(96.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ── Género ─────────────────────────────────────────────────────────
        FieldLabel("Genero:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

            GenderButton(
                selected = genero == "M",
                onClick = { genero = "M" },
                emoji = "♂",
                tint = AzulGenero
            )

            GenderButton(
                selected = genero == "F",
                onClick = { genero = "F" },
                emoji = "♀",
                tint = RosaGenero
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        
        FieldLabel("Contraseña:")
        RegisterTextField(
            value = password,
            onValueChange = { password = it },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = onCreateAccountClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AzulApp,
                contentColor   = Color.White
            )
        ) {
            Text(
                text = "Crear Cuenta",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "¿Ya tienes una cuenta?  ",
                fontSize = 14.sp,
                color = GrisPlaceholder
            )
            TextButton(
                onClick = onLoginClick,
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "Iniciar Sesion",
                    fontSize = 14.sp,
                    color = AzulApp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun FieldLabel(text: String) {
    Text(
        text = text,
        fontSize = 15.sp,
        fontWeight = FontWeight.SemiBold,
        color = ColorLabel,
        modifier = Modifier.padding(bottom = 6.dp)
    )
}

@Composable
private fun RegisterTextField(
    value: String,
    onValueChange: (String) -> Unit,
    visualTransformation: androidx.compose.ui.text.input.VisualTransformation =
        androidx.compose.ui.text.input.VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        singleLine = true,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor    = GrisBorde,
            focusedBorderColor      = AzulApp,
            unfocusedContainerColor = Color.White,
            focusedContainerColor   = Color.White,
            cursorColor             = AzulApp
        )
    )
}


@Composable
private fun DatePartField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.height(52.dp),
        singleLine = true,
        placeholder = {
            Text(
                text = placeholder,
                color = GrisPlaceholder,
                fontSize = 14.sp
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor    = GrisBorde,
            focusedBorderColor      = AzulApp,
            unfocusedContainerColor = Color.White,
            focusedContainerColor   = Color.White,
            cursorColor             = AzulApp
        )
    )
}


@Composable
private fun GenderButton(
    selected: Boolean,
    onClick: () -> Unit,
    emoji: String,
    tint: Color
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.size(52.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(
            width = if (selected) 2.dp else 1.dp,
            color = if (selected) tint else GrisBorde
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (selected) tint.copy(alpha = 0.12f) else Color.White
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = emoji,
            fontSize = 24.sp,
            color = tint
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterScreenPreview() {
    MaterialTheme {
        RegisterScreen()
    }
}