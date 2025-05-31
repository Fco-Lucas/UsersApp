package com.lcsz.first.ui.screens.register

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lcsz.first.navigation.AppScreen
import com.lcsz.first.ui.components.AppButton
import com.lcsz.first.ui.viewmodel.RegisterViewModel
import com.lcsz.first.util.masks.CpfVisualTransformation
import androidx.compose.ui.text.TextStyle
import com.lcsz.first.R

@Composable
fun RegisterScreen(
    navController: NavController,
    registerViewModel: RegisterViewModel = hiltViewModel()
) {
    val name = registerViewModel.name
    val cpf = registerViewModel.cpf
    val password = registerViewModel.password
    val confirmPassword = registerViewModel.confirmPassword

    val registerState by registerViewModel.registerUiState.collectAsState()

    val context = LocalContext.current

    val MAX_CPF_LENGTH = 11

    // LaunchedEffect para reagir a mudanças no loginState (ex: navegar após sucesso, mostrar Toast)
    LaunchedEffect(registerState) {
        when (val currentState = registerState) {
            is RegisterUiState.Success -> {
                // Agora currentState.user contém o objeto User do domínio
                Toast.makeText(context, "Cadastro realizado com sucesso", Toast.LENGTH_LONG).show()
                navController.navigate(AppScreen.LoginScreen.route) {
                    popUpTo(AppScreen.RegisterScreen.route) { inclusive = true }
                }
                registerViewModel.resetRegisterUiState()
            }
            is RegisterUiState.Error -> {
                Toast.makeText(context, currentState.message, Toast.LENGTH_LONG).show()
                registerViewModel.resetRegisterUiState()
            }
            is RegisterUiState.Loading -> {
                // A UI para Loading já é tratada no corpo do Column
            }
            is RegisterUiState.Idle -> {
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
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Cadastro",
                style = TextStyle( // Você pode omitir androidx.compose.ui.text. se TextStyle estiver importado
                    fontSize = 20.sp,           // Tamanho da fonte CORRIGIDO
                    fontWeight = FontWeight.Bold // Negrito (correto)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { registerViewModel.onNameChange(it) },
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth(),
                enabled = registerState !is RegisterUiState.Loading,
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Blue,
                    focusedLabelColor = Color.Blue,
                    cursorColor = Color.Blue,
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    autoCorrect = false
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = cpf,
                onValueChange = { newValue ->
                    if(newValue.length <= MAX_CPF_LENGTH) {
                        // Atualiza o ViewModel apenas com números, permitindo apagar
                        if (newValue.all { it.isDigit() } || newValue.isEmpty()) {
                            registerViewModel.onCpfChange(newValue)
                        }
                    }
                },
                label = { Text("CPF") },
                modifier = Modifier.fillMaxWidth(),
                enabled = registerState !is RegisterUiState.Loading, // Desabilita se estiver carregando
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
                onValueChange = { registerViewModel.onPasswordChange(it) },
                label = { Text("Senha") },
                modifier = Modifier.fillMaxWidth(),
                enabled = registerState !is RegisterUiState.Loading,
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

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { registerViewModel.onConfirmPasswordChange(it) },
                label = { Text("Confirmar Senha") },
                modifier = Modifier.fillMaxWidth(),
                enabled = registerState !is RegisterUiState.Loading,
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

            if(registerState is RegisterUiState.Loading) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(24.dp))
            }

            AppButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Cadastrar",
                onClick = {
                    registerViewModel.onRegisterClick()
                },
                enabled = registerState !is RegisterUiState.Loading
            )

            Spacer(modifier = Modifier.height(16.dp))

            AppButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Voltar para Login",
                onClick = {
                    if(registerState !is RegisterUiState.Loading) {
                        navController.navigate(AppScreen.LoginScreen.route)
                    }
                },
                buttonColor = Color.DarkGray,
                enabled = registerState !is RegisterUiState.Loading
            )
        }
    }
}