package com.lcsz.first.ui.screens.userlist.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.lcsz.first.ui.components.AppButton
import com.lcsz.first.util.masks.CpfVisualTransformation

@Composable
fun CreateUserModal(
    createUserName: String,
    createUserCpf: String,
    createUserPassword: String,
    createUserConfirmPassword: String,
    onCreateUserNameChange: (String) -> Unit,
    onCreateUserCpfChange: (String) -> Unit,
    onCreateUserPasswordChange: (String) -> Unit,
    onCreateUserConfirmPasswordChange: (String) -> Unit,
    isCreatingUser: Boolean,
    errorMessage: String?,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    val MAX_CPF_LENGTH = 11

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Criar Novo Usuário") },
        text = {
            Column {
                OutlinedTextField(
                    value = createUserName,
                    onValueChange = onCreateUserNameChange,
                    label = { Text("Nome") },
                    singleLine = true,
                    enabled = !isCreatingUser,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done,
                        autoCorrect = false
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary,
                    )
                )

                OutlinedTextField(
                    value = createUserCpf,
                    onValueChange = { newValue ->
                        if(newValue.length <= MAX_CPF_LENGTH) {
                            // Atualiza o ViewModel apenas com números, permitindo apagar
                            if (newValue.all { it.isDigit() } || newValue.isEmpty()) {
                                onCreateUserCpfChange(newValue)
                            }
                        }
                    },
                    label = { Text("CPF") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isCreatingUser, // Desabilita se estiver carregando
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors( // 2. Alterando as cores
                        focusedBorderColor = MaterialTheme.colorScheme.primary,          // Cor da borda quando focado
                        focusedLabelColor = MaterialTheme.colorScheme.primary,           // Cor do label quando focado
                        cursorColor = MaterialTheme.colorScheme.primary,
                    ),
                    keyboardOptions = KeyboardOptions( // 3. Para tirar autocomplete e configurar teclado
                        keyboardType = KeyboardType.Number, // Teclado numérico, bom para CPF
                        imeAction = ImeAction.Next,         // Ação do botão "Enter" do teclado (vai para o próximo campo)
                        autoCorrect = false                 // Desabilita a correção automática (ajuda a "tirar o autocomplete")
                    ),
                    visualTransformation = CpfVisualTransformation()
                )

                OutlinedTextField(
                    value = createUserPassword,
                    onValueChange = onCreateUserPasswordChange,
                    label = { Text("Senha") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isCreatingUser,
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next,
                        autoCorrect = false
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary,
                    )
                )

                OutlinedTextField(
                    value = createUserConfirmPassword,
                    onValueChange = onCreateUserConfirmPasswordChange,
                    label = { Text("Confirmar Senha") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isCreatingUser,
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                        autoCorrect = false
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary,
                    )
                )

                if (isCreatingUser) {
                    Spacer(modifier = Modifier.height(8.dp))
                    CircularProgressIndicator()
                }
                errorMessage?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
            }
        },
        confirmButton = {
            AppButton(
                text = "Salvar",
                onClick = onConfirm,
                enabled = !isCreatingUser,
                buttonColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.heightIn(max = 36.dp)
            )
        },
        dismissButton = {
            AppButton(
                text = "Cancelar",
                onClick = onDismiss,
                enabled = !isCreatingUser,
                buttonColor = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.heightIn(max = 36.dp)
            )
        }
    )
}