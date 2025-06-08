package com.lcsz.first.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.lcsz.first.navigation.BottomNavItem

@Composable
fun AppBottomNavigationBar(
    navController: NavController,
    items: List<BottomNavItem>
) {
    NavigationBar { // Componente do Material 3 para a barra inferior
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route, // O item está selecionado se a rota atual for a dele
                onClick = {
                    navController.navigate(item.route) {
                        // Volta para a tela inicial do grafo para evitar empilhar telas
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        // Evita criar uma nova instância da mesma tela se ela já estiver no topo
                        launchSingleTop = true
                        // Restaura o estado ao selecionar um item previamente selecionado
                        restoreState = true
                    }
                },
                icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                label = { Text(text = item.label) }
            )
        }
    }
}