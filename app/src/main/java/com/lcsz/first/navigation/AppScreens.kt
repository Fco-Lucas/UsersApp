package com.lcsz.first.navigation

sealed class AppScreen(val route: String) {
    data object LoginScreen : AppScreen("login_screen")
    data object RegisterScreen : AppScreen("register_screen")
    data object HomeScreen : AppScreen("homeScreen")
}