package com.lcsz.first.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val getUserListUseCase: GetUserListUseCase
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
}