package com.lcsz.first.domain.repository

import com.lcsz.first.domain.model.User
import com.lcsz.first.util.Resource
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun create(name: String, cpf: String, password: String): Flow<Resource<User>>
}