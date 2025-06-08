package com.lcsz.first.navigation

sealed class AppScreen(val route: String) {
    data object LoginScreen : AppScreen("login_screen")
    data object RegisterScreen : AppScreen("register_screen")
    data object UserListScreen : AppScreen("userList_screen")
    data object ProfileScreen : AppScreen("profile_screen")
    data object SettingsScreen : AppScreen("settings_screen")
}