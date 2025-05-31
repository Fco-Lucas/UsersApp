package com.lcsz.first.ui.screens.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.lcsz.first.navigation.AppScreen
import com.lcsz.first.ui.components.AppButton
import com.lcsz.first.ui.theme.AppTheme
import com.lcsz.first.ui.viewmodel.LoginViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lcsz.first.util.masks.CpfVisualTransformation
import com.lcsz.first.R

@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val cpf = loginViewModel.cpf
    val password = loginViewModel.password

    // Observa o loginUiState do ViewModel
    val loginState by loginViewModel.loginUiState.collectAsState()

    val context = LocalContext.current

    val MAX_CPF_LENGTH = 11

    LaunchedEffect(loginState) {
        when (val currentState = loginState) {
            is LoginUiState.Success -> {
                // Agora currentState.user contém o objeto User do domínio
                Toast.makeText(context, "Login bem-sucedido! Token: ${currentState.token}", Toast.LENGTH_LONG).show()
                navController.navigate(AppScreen.HomeScreen.route) {
                    popUpTo(AppScreen.LoginScreen.route) { inclusive = true }
                }
                loginViewModel.resetLoginState()
            }
            is LoginUiState.Error -> {
                Toast.makeText(context, currentState.message, Toast.LENGTH_LONG).show()
                loginViewModel.resetLoginState()
            }
            is LoginUiState.Loading -> {
                // A UI para Loading já é tratada no corpo do Column
            }
            is LoginUiState.Idle -> {
                // Nada a fazer aqui
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.fundo2claro),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxWidth() // Ocupa a largura máxima
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Login",
                style = TextStyle( // Você pode omitir androidx.compose.ui.text. se TextStyle estiver importado
                    fontSize = 20.sp,           // Tamanho da fonte CORRIGIDO
                    fontWeight = FontWeight.Bold // Negrito (correto)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = cpf,
                onValueChange = { newValue ->
                    if(newValue.length <= MAX_CPF_LENGTH) {
                        // Atualiza o ViewModel apenas com números, permitindo apagar
                        if (newValue.all { it.isDigit() } || newValue.isEmpty()) {
                            loginViewModel.onCpfChange(newValue)
                        }
                    }
                },
                label = { Text("CPF") },
                modifier = Modifier.fillMaxWidth(),
                enabled = loginState !is LoginUiState.Loading, // Desabilita se estiver carregando
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors( // 2. Alterando as cores
                    focusedBorderColor = Color.Blue,          // Cor da borda quando focado
                    focusedLabelColor = Color.Blue,           // Cor do label quando focado
                    cursorColor = Color.Blue,
                ),
                keyboardOptions = KeyboardOptions( // 3. Para tirar autocomplete e configurar teclado
                    keyboardType = KeyboardType.Number, // Teclado numérico, bom para CPF
                    imeAction = ImeAction.Next,         // Ação do botão "Enter" do teclado (vai para o próximo campo)
                    autoCorrect = false                 // Desabilita a correção automática (ajuda a "tirar o autocomplete")
                ),
                visualTransformation = CpfVisualTransformation()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { loginViewModel.onPasswordChange(it) },
                label = { Text("Senha") },
                modifier = Modifier.fillMaxWidth(),
                enabled = loginState !is LoginUiState.Loading,
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                    autoCorrect = false
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Blue,
                    focusedLabelColor = Color.Blue,
                    cursorColor = Color.Blue,
                )
            )
            Spacer(modifier = Modifier.height(24.dp))

            if (loginState is LoginUiState.Loading) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(24.dp))
            }

            AppButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Entrar",
                onClick = {
                    loginViewModel.onLoginClick()
                },
                enabled = loginState !is LoginUiState.Loading // Desabilita botão se estiver carregando
            )
            Spacer(modifier = Modifier.height(16.dp))

            AppButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Ir para Cadastro",
                onClick = {
                    if (loginState !is LoginUiState.Loading) { // Só navega se não estiver carregando
                        navController.navigate(AppScreen.RegisterScreen.route)
                    }
                },
                buttonColor = Color.DarkGray,
                enabled = loginState !is LoginUiState.Loading
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    AppTheme {
        Text("Preview da Tela de Login (sem NavController)")
    }
}