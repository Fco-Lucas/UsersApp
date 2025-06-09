package com.lcsz.first.ui.screens.userlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lcsz.first.ui.screens.userlist.components.CreateUserModal
import com.lcsz.first.ui.screens.userlist.components.UserCard
import com.lcsz.first.ui.viewmodel.UserListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(
    navController: NavController,
    viewModel: UserListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val createUserName = viewModel.createUserName
    val createUserCpf = viewModel.createUserCpf
    val createUserPassword = viewModel.createUserPassword
    val createUserConfirmPassword = viewModel.createUserConfirmPassword

    if(uiState.isCreateUserModalOpen) {
        CreateUserModal(
            createUserName = createUserName,
            createUserCpf = createUserCpf,
            createUserPassword = createUserPassword,
            createUserConfirmPassword = createUserConfirmPassword,
            onCreateUserNameChange = viewModel::onCreateUserNameChange,
            onCreateUserCpfChange = viewModel::onCreateUserCpfChange,
            onCreateUserPasswordChange = viewModel::onCreateUserPasswordChange,
            onCreateUserConfirmPasswordChange = viewModel::onCreateUserConfirmPasswordChange,
            isCreatingUser = uiState.isCreatingUser,
            errorMessage = uiState.createUserError,
            onConfirm = viewModel::onCreateUser,
            onDismiss = viewModel::onDismissCreateUserModal
        )
    }

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("Usuários") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Blue,
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onShowCreateUserModal() },
                containerColor = Color.Blue,
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Adicionar usuário",
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            // Mostra o indicador de progresso enquanto carrega
            if (uiState.isLoading) {
                CircularProgressIndicator()
            }

            // Mostra a lista se não estiver carregando e houver usuários
            if (!uiState.isLoading && uiState.users.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(items = uiState.users, key = { user -> user.id }) { user ->
                        UserCard(user = user)
                    }
                }
            }

            // Mostra mensagem se a lista estiver vazia e não estiver carregando
            if (!uiState.isLoading && uiState.users.isEmpty() && uiState.errorMessage == null) {
                Text("Nenhum usuário encontrado.")
            }

            // Mostra mensagem de erro se houver
            uiState.errorMessage?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}