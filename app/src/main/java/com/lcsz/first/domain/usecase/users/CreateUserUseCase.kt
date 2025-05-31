package com.lcsz.first.domain.usecase.users

import com.lcsz.first.domain.model.User
import com.lcsz.first.domain.repository.UserRepository
import com.lcsz.first.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(name: String, cpf: String, password: String): Flow<Resource<User>> {
        return userRepository.create(name, cpf, password)
    }
}