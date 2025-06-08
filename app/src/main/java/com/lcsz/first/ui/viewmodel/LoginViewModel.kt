package com.lcsz.first.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lcsz.first.data.local.SessionManager
import com.lcsz.first.domain.usecase.auth.LoginUserUseCase // Importe o Caso de Uso
import com.lcsz.first.ui.screens.login.LoginUiState
import com.lcsz.first.util.Resource // Importe o Resource wrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn // Para coletar o Flow
import kotlinx.coroutines.flow.onEach  // Para coletar o Flow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUserUseCase: LoginUserUseCase, // Agora injeta o Caso de Uso
    private val sessionManager: SessionManager
) : ViewModel() {

    private val TAG = "LoginViewModel"

    var cpf by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set

    private val _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginUiState: StateFlow<LoginUiState> = _loginUiState.asStateFlow()

    fun onCpfChange(novoCpf: String) { cpf = novoCpf }
    fun onPasswordChange(novaSenha: String) { password = novaSenha }

    fun onLoginClick() {
        Log.d(TAG, "onLoginClick chamado com CPF: $cpf")

        if(cpf.isBlank()) {
            _loginUiState.value = LoginUiState.Error("CPF não pode estar vazio.")
            return
        }

        if(password.isBlank()) {
            _loginUiState.value = LoginUiState.Error("Senha não pode estar vazia.")
            return
        }

        // Chama o caso de uso, que retorna um Flow
        loginUserUseCase(cpf = cpf, password = password)
            .onEach { resource -> // Coleta cada emissão do Flow
                when (resource) {
                    is Resource.Loading -> {
                        _loginUiState.value = LoginUiState.Loading
                    }
                    is Resource.Success -> {
                        // resource.data aqui é o objeto User
                        if (resource.data != null) {
                            Log.i(TAG, "Login bem-sucedido: Token: ${resource.data}")
                            sessionManager.saveAuthToken(resource.data)
                            _loginUiState.value = LoginUiState.Success(resource.data)
                        } else {
                            // Isso não deveria acontecer se a lógica do Resource.Success garantir data não nula
                            Log.e(TAG, "Sucesso no login, mas dados do usuário nulos.")
                            _loginUiState.value = LoginUiState.Error("Erro ao obter dados do usuário após login.")
                        }
                    }
                    is Resource.Error -> {
                        Log.e(TAG, "Falha no login: ${resource.message}")
                        _loginUiState.value = LoginUiState.Error(resource.message ?: "Ocorreu um erro desconhecido.")
                    }
                }
            }
            .launchIn(viewModelScope) // Inicia a coleta do Flow no viewModelScope
    }

    fun resetLoginState() {
        _loginUiState.value = LoginUiState.Idle
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "onCleared")
    }
}