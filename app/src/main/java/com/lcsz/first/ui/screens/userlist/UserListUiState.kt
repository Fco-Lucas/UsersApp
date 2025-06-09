package com.lcsz.first.ui.screens.userlist

import com.lcsz.first.domain.model.User

data class UserListUiState(
    // Listagem dos usuários
    val isLoading: Boolean = false,
    val users: List<User> = emptyList(),
    val errorMessage: String? = null,

    // Modal para criar usuários
    val isCreateUserModalOpen: Boolean = false,
    val isCreatingUser: Boolean = false,
    val createUserError: String? = null
)