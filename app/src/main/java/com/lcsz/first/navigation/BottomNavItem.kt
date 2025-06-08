package com.lcsz.first.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Home : BottomNavItem(
        route = AppScreen.UserListScreen.route, // Reutilizando a rota que já temos
        icon = Icons.Default.Home,
        label = "Home"
    )

    object Profile : BottomNavItem(
        route = "profile_screen", // Nova rota para uma futura tela de perfil
        icon = Icons.Default.Person,
        label = "Perfil"
    )

    object Settings : BottomNavItem(
        route = "settings_screen", // Nova rota para uma futura tela de configurações
        icon = Icons.Default.Settings,
        label = "Ajustes"
    )
}