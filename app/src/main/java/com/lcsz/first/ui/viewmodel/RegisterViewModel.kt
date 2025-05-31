package com.lcsz.first.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lcsz.first.domain.usecase.users.CreateUserUseCase
import com.lcsz.first.ui.screens.register.RegisterUiState
import com.lcsz.first.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: CreateUserUseCase // Injeta o caso de uso
) : ViewModel() // Implementa a classe ViewModel
{
    private val TAG = "RegisterViewModel"

    var name by mutableStateOf("")
        private set
    var cpf by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var confirmPassword by mutableStateOf("")
        private set

    private val _registerUiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)
    val registerUiState: StateFlow<RegisterUiState> = _registerUiState.asStateFlow()

    fun onNameChange(newName: String) { name = newName }
    fun onCpfChange(newCpf: String) { cpf = newCpf }
    fun onPasswordChange(newPassword: String) { password = newPassword }
    fun onConfirmPasswordChange(newConfirmPassword: String) { confirmPassword = newConfirmPassword }

    fun onRegisterClick() {
        if(name.isBlank()) {
            _registerUiState.value = RegisterUiState.Error("Nome não pode ser vazio")
            return
        }
        if(cpf.isBlank()) {
            _registerUiState.value = RegisterUiState.Error("CPF não pode ser vazio")
            return
        }
        if(password.isBlank()) {
            _registerUiState.value = RegisterUiState.Error("Senha não pode ser vazia")
            return
        }
        if(confirmPassword.isBlank()) {
            _registerUiState.value = RegisterUiState.Error("Confirmação de senha não pode ser vazia")
            return
        }
        if(password != confirmPassword) {
            _registerUiState.value = RegisterUiState.Error("Senhas não conferem")
            return
        }

        // Chama o caso de uso, que retorna um Flow
        registerUseCase(name = name, cpf = cpf, password = password)
            .onEach { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _registerUiState.value = RegisterUiState.Loading
                    }
                    is Resource.Success -> {
                        // resource.data aqui é o objeto User
                        if (resource.data != null) {
                            Log.i(TAG, "Login bem-sucedido: Token: ${resource.data}")
                            _registerUiState.value = RegisterUiState.Success(resource.data)
                        } else {
                            Log.e(TAG, "Sucesso no cadastro, porém os dados do usuário não foram obtidos.")
                            _registerUiState.value = RegisterUiState.Error("Erro ao obter dados do usuário após cadastro bem-sucedido.")
                        }
                    }
                    is Resource.Error -> {
                        Log.e(TAG, "Falha no login: ${resource.message}")
                        _registerUiState.value = RegisterUiState.Error(resource.message ?: "Ocorreu um erro desconhecido.")
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun resetRegisterUiState () {
        _registerUiState.value = RegisterUiState.Idle
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "onCleared")
    }
}