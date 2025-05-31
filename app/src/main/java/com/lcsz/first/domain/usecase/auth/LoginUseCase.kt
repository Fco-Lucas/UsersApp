package com.lcsz.first.domain.usecase.auth

import com.lcsz.first.domain.repository.AuthRepository
import com.lcsz.first.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject // Para Hilt injetar o repositório aqui

class LoginUserUseCase @Inject constructor( // Hilt injetará a implementação do UserRepository
    private val authRepository: AuthRepository
) {
    operator fun invoke(cpf: String, password: String): Flow<Resource<String>> {
        return authRepository.login(cpf, password)
    }
}