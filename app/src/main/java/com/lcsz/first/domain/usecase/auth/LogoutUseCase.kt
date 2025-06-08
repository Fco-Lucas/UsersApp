package com.lcsz.first.domain.usecase.auth

import com.lcsz.first.data.local.SessionManager
import javax.inject.Inject

class LogoutUserUseCase @Inject constructor (
    private val sessionManager: SessionManager
) {
    operator fun invoke() {
        sessionManager.clearAuthToken()
    }
}