package LosPrimos.Durango.calculadoragastos.ui.screens


import LosPrimos.Durango.calculadoragastos.ui.theme.AzulApp
import LosPrimos.Durango.calculadoragastos.ui.theme.ColorBarraSuperior
import LosPrimos.Durango.calculadoragastos.ui.theme.FondoBlanco
import LosPrimos.Durango.calculadoragastos.ui.theme.GrisBorde
import LosPrimos.Durango.calculadoragastos.ui.theme.GrisTexto
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginClick: () -> Unit = {},
    onFingerprintClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {}
) {
    var email    by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Scaffold(
        topBar = { LoginTopBar() },
        containerColor = FondoBlanco
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Toma el control de\ntus finanzas",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.Black,
                lineHeight = 36.sp
            )


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )


            LoginTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = "Correo electrónico",
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Email,
                        contentDescription = "Email",
                        tint = GrisTexto
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(16.dp))


            LoginTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = "Contraseña",
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = "Contraseña",
                        tint = GrisTexto
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(32.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Button(
                    onClick = onLoginClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AzulApp,
                        contentColor   = Color.White
                    )
                ) {
                    Text(
                        text = "Iniciar sesión",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }


                OutlinedButton(
                    onClick = onFingerprintClick,
                    modifier = Modifier.size(56.dp),
                    shape = CircleShape,
                    border = BorderStroke(1.5.dp, AzulApp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent,
                        contentColor   = AzulApp
                    ),
                    contentPadding = PaddingValues(0.dp)
                ) {
                }
            }

            Spacer(modifier = Modifier.height(32.dp))


            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No tienes una cuenta?  ",
                    fontSize = 14.sp,
                    color = GrisTexto
                )
                TextButton(
                    onClick = onRegisterClick,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "Regístrate",
                        fontSize = 14.sp,
                        color = AzulApp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginTopBar() {
    TopAppBar(
        title = {
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = ColorBarraSuperior
        )
    )
}

@Composable
private fun LoginTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: androidx.compose.ui.text.input.VisualTransformation =
        androidx.compose.ui.text.input.VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        placeholder = {
            Text(
                text = placeholder,
                color = GrisTexto,
                fontSize = 15.sp
            )
        },
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = GrisBorde,
            focusedBorderColor   = AzulApp,
            unfocusedContainerColor = Color.White,
            focusedContainerColor   = Color.White,
            cursorColor             = AzulApp
        )
    )
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    MaterialTheme {
        LoginScreen()
    }
}