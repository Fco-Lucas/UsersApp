package com.lcsz.first.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lcsz.first.ui.components.AppBottomNavigationBar
import com.lcsz.first.ui.screens.login.LoginScreen
import com.lcsz.first.ui.screens.register.RegisterScreen
import com.lcsz.first.ui.screens.settings.SettingsScreen
import com.lcsz.first.ui.screens.userlist.UserListScreen
import com.lcsz.first.ui.viewmodel.AuthState
import com.lcsz.first.ui.viewmodel.MainViewModel

/**
 * PONTO DE ENTRADA PRINCIPAL DA NAVEGAÇÃO DO APP
 * Esta é a função que sua MainActivity chama.
 * Ela decide qual fluxo de navegação mostrar (Autenticado ou Não Autenticado).
 */
@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel
) {
    // Observa o estado de autenticação a partir do ViewModel principal
    val authState by mainViewModel.authState.collectAsState()

    // O 'when' decide qual "mundo" o usuário verá
    when (authState) {
        AuthState.LOADING -> {
            // Não mostramos nada aqui, pois a Splash Screen do Sistema ainda está visível.
            // Um Box vazio garante que nada pisque na tela durante a transição.
            Box(modifier = Modifier.fillMaxSize())
        }
        AuthState.AUTHENTICATED -> {
            // Usuário está logado: Mostra a tela principal com a Bottom Bar
            MainScreen(mainViewModel = mainViewModel)
        }
        AuthState.UNAUTHENTICATED -> {
            // Usuário NÃO está logado: Mostra o fluxo de Login/Registro
            AuthNavigation(mainViewModel = mainViewModel, modifier = modifier)
        }
    }
}

/**
 * Grafo de navegação para usuários NÃO AUTENTICADOS.
 */
@Composable
private fun AuthNavigation(mainViewModel: MainViewModel, modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AppScreen.LoginScreen.route,
        modifier = modifier
    ) {
        composable(route = AppScreen.LoginScreen.route) {
            LoginScreen(
                navController = navController,
                onLoginSuccess = {
                    // Após o login, notifica o MainViewModel para reavaliar o estado.
                    // Isso fará com que o 'when' em AppNavigation mude para AUTHENTICATED.
                    mainViewModel.checkAuthenticationState()
                }
            )
        }
        composable(route = AppScreen.RegisterScreen.route) {
            // A tela de registro pode ter um callback similar se ela logar o usuário automaticamente
            RegisterScreen(navController = navController)
        }
    }
}


/**
 * Composable da tela principal para usuários LOGADOS.
 * Contém o Scaffold, a Bottom Navigation Bar e o NavHost interno.
 */
@Composable
private fun MainScreen(mainViewModel: MainViewModel) {
    val navController = rememberNavController()
    val navigationItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Profile,
        BottomNavItem.Settings
    )

    Scaffold(
        bottomBar = {
            AppBottomNavigationBar(navController = navController, items = navigationItems)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = BottomNavItem.Home.route) {
                UserListScreen(navController = navController)
            }
            composable(route = BottomNavItem.Profile.route) {
                GenericScreen(text = "Tela de Perfil")
            }
            composable(route = BottomNavItem.Settings.route) {
                SettingsScreen(
                    onLogout = {
                        // Após o logout, notifica o MainViewModel para reavaliar o estado.
                        // Isso fará com que o 'when' em AppNavigation mude para UNAUTHENTICATED.
                        mainViewModel.checkAuthenticationState()
                    }
                )
            }
        }
    }
}

// --- Componentes Auxiliares ---
@Composable
private fun GenericScreen(text: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = text)
    }
}
