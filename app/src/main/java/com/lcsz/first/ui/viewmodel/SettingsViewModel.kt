package com.lcsz.first.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.lcsz.first.domain.usecase.auth.LogoutUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val logoutUserUseCase: LogoutUserUseCase
) : ViewModel() {
    fun onLogoutClicked() {
        logoutUserUseCase()
    }
}