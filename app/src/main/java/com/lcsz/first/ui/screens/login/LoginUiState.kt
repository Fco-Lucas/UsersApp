package com.lcsz.first.ui.screens.login

sealed class LoginUiState {
    object Idle: LoginUiState() // Estado inicial, nada acontecendo
    object Loading: LoginUiState() // Tentativa de login em progresso
    data class Success(val token: String): LoginUiState() // Login bem-sucedido
    data class Error(val message: String): LoginUiState() // Erro no login
}