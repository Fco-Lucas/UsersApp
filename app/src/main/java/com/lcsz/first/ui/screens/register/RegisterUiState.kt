package com.lcsz.first.ui.screens.register

import com.lcsz.first.domain.model.User

sealed class RegisterUiState {
    object Idle : RegisterUiState()
    object Loading : RegisterUiState()
    data class Success(val user: User): RegisterUiState()
    data class Error(val message: String) : RegisterUiState()
}