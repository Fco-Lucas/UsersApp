package com.lcsz.first

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.lcsz.first.navigation.AppNavigation
import com.lcsz.first.ui.theme.AppTheme
import com.lcsz.first.ui.viewmodel.AuthState
import com.lcsz.first.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Obtém a instância do MainViewModel que será usada na AppNavigation
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        // 1. A chamada para installSplashScreen() deve vir ANTES de super.onCreate()
        installSplashScreen().setKeepOnScreenCondition {
            // 2. Mantenha a splash screen visível até que o authState NÃO seja mais LOADING
            mainViewModel.authState.value == AuthState.LOADING
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                AppNavigation(mainViewModel = mainViewModel)
            }
        }
    }
}