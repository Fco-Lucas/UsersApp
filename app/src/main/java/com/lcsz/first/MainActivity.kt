package com.lcsz.first

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.lcsz.first.navigation.AppScreen
import com.lcsz.first.ui.screens.login.LoginScreen
import com.lcsz.first.ui.screens.register.RegisterScreen
import com.lcsz.first.ui.screens.home.HomeScreen
import com.lcsz.first.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
//                Scaffold( modifier = Modifier.fillMaxSize() ) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController() // Cria e lembra do controlador de navegação

    NavHost(
        navController = navController,
        startDestination = AppScreen.LoginScreen.route, // Define a tela inicial
        modifier = modifier // Aplica o modifier (ex: padding do Scaffold)
    ) {
        // Define cada tela navegável
        composable(route = AppScreen.LoginScreen.route) {
            LoginScreen(navController = navController)
        }
        composable(route = AppScreen.RegisterScreen.route) {
            RegisterScreen(navController = navController)
        }
        composable(route = AppScreen.HomeScreen.route) {
            HomeScreen(navController = navController)
        }
    }
}