package com.lcsz.first.ui.screens.userlist

import com.lcsz.first.domain.model.User

data class UserListUiState(
    val isLoading: Boolean = false,
    val users: List<User> = emptyList(),
    val errorMessage: String? = null
)