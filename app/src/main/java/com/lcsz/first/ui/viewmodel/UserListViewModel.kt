package com.lcsz.first.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lcsz.first.domain.usecase.users.CreateUserUseCase
import com.lcsz.first.domain.usecase.users.GetUserListUseCase
import com.lcsz.first.ui.screens.userlist.UserListUiState
import com.lcsz.first.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val getUserListUseCase: GetUserListUseCase,
    private val createUserUseCase: CreateUserUseCase
) : ViewModel() {
    private val TAG = "UserListViewModel"

    private val _uiState = MutableStateFlow(UserListUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadUsers()
    }

    fun loadUsers() {
        getUserListUseCase().onEach { resource ->
            when (resource) {
                is Resource.Loading -> {
                    Log.d(TAG, "Carregando lista de usuários...")
                    _uiState.update { it.copy(isLoading = true) }
                }
                is Resource.Success -> {
                    Log.i(TAG, "Usuários carregados com sucesso: ${resource.data?.size} usuários.")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            users = resource.data ?: emptyList(),
                            errorMessage = null
                        )
                    }
                }
                is Resource.Error -> {
                    Log.e(TAG, "Erro ao carregar usuários: ${resource.message}")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = resource.message ?: "Ocorreu um erro desconhecido."
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    // Modal para criar usuários
    var createUserName by mutableStateOf("")
        private set
    var createUserCpf by mutableStateOf("")
        private set
    var createUserPassword by mutableStateOf("")
        private set
    var createUserConfirmPassword by mutableStateOf("")
        private set

    fun onShowCreateUserModal() {
        _uiState.update { it.copy(isCreateUserModalOpen = true) }
    }

    fun onDismissCreateUserModal() {
        // Limpa os campos e erros ao fechar a modal
        createUserName = ""
        createUserCpf = ""
        createUserPassword = ""
        createUserConfirmPassword = ""
        _uiState.update { it.copy(isCreateUserModalOpen = false, createUserError = null) }
    }

    fun onCreateUserNameChange (name: String) {
        createUserName = name
    }
    fun onCreateUserCpfChange (cpf: String) {
        createUserCpf = cpf
    }
    fun onCreateUserPasswordChange (password: String) {
        createUserPassword = password
    }
    fun onCreateUserConfirmPasswordChange (confirmPassword: String) {
        createUserConfirmPassword = confirmPassword
    }

    fun onCreateUser() {
        // 1. Validação do Nome
        if (createUserName.isBlank()) {
            // Correção: Atualiza o createUserError dentro do UserListUiState
            _uiState.update { it.copy(createUserError = "Nome não pode ser vazio") }
            return
        }

        // 2. Validação do CPF
        if (createUserCpf.isBlank()) {
            _uiState.update { it.copy(createUserError = "CPF não pode ser vazio") }
            return
        }

        // 3. Validação da Senha
        if (createUserPassword.isBlank()) {
            _uiState.update { it.copy(createUserError = "Senha não pode ser vazia") }
            return
        }

        // 4. Validação da Confirmação de Senha
        if (createUserConfirmPassword.isBlank()) {
            _uiState.update { it.copy(createUserError = "Confirmação de senha não pode ser vazia") }
            return
        }

        // 5. Validação de Senhas Iguais
        if (createUserPassword != createUserConfirmPassword) {
            _uiState.update { it.copy(createUserError = "As senhas não conferem") }
            return
        }

        // Se todas as validações passaram, limpa qualquer erro anterior e inicia a chamada à API
        _uiState.update { it.copy(createUserError = null) }

        // Supondo que você precise da senha para o CreateUserUseCase
        createUserUseCase(name = createUserName, cpf = createUserCpf, password = createUserPassword)
            .onEach { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isCreatingUser = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update { it.copy(isCreatingUser = false) }
                        Log.i(TAG, "Usuário criado com sucesso: ${resource.data}")
                        onDismissCreateUserModal() // Fecha a modal
                        loadUsers() // Recarrega a lista para mostrar o novo usuário
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isCreatingUser = false,
                                createUserError = resource.message ?: "Erro desconhecido"
                            )
                        }
                    }
                }
            }.launchIn(viewModelScope)
    }
}