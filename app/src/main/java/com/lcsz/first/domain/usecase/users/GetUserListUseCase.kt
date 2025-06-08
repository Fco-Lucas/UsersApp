package com.lcsz.first.domain.usecase.users

import com.lcsz.first.domain.model.User
import com.lcsz.first.domain.repository.UserRepository
import com.lcsz.first.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserListUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<Resource<List<User>>> {
        return userRepository.getUsers()
    }
}