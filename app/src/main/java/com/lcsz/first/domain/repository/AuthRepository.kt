package com.lcsz.first.domain.repository

import com.lcsz.first.util.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(cpf: String, password: String): Flow<Resource<String>>
}