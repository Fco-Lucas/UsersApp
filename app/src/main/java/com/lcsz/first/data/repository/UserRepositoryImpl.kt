package com.lcsz.first.data.repository

import android.util.Log
import com.lcsz.first.data.mapper.toDomainUser
import com.lcsz.first.data.remote.dto.users.UserCreateRequest
import com.lcsz.first.data.remote.safeApiCall
import com.lcsz.first.data.remote.service.UserApiService
import com.lcsz.first.domain.model.User
import com.lcsz.first.domain.repository.UserRepository
import com.lcsz.first.util.Resource
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApiService: UserApiService,
    private val moshi: Moshi
) : UserRepository {
    private val TAG = "UserRepositoryImpl"

    override fun create(name: String, cpf: String, password: String): Flow<Resource<User>> {
        return safeApiCall(
            moshi = moshi,
            apiCall = { userApiService.register(UserCreateRequest(name, cpf, password)) },
            mapper = { userCreateResponse -> userCreateResponse.toDomainUser() }
        ).onEach { resource ->
            when (resource) {
                is Resource.Success -> Log.i(TAG, "Cadastro para o usuário $cpf bem-sucedido (via safeApiCall).")
                is Resource.Error -> Log.e(TAG, "Cadastro para o usuário $cpf falhou (via safeApiCall): ${resource.message}")
                is Resource.Loading -> Log.d(TAG, "Cadastro para o usuário $cpf carregando (via safeApiCall)...")
            }
        }
    }

    override fun getUsers(): Flow<Resource<List<User>>> {
        return safeApiCall (
            moshi = moshi,
            apiCall = { userApiService.getUsers() },
            mapper = { userDtoList -> userDtoList.map { it.toDomainUser() } }
        ).onEach { resource ->
            when (resource) {
                is Resource.Success -> Log.i(TAG, "Lista de usuários recuperada com sucesso (via safeApiCall).")
                is Resource.Error -> Log.e(TAG, "Erro ao recuperar a lista de usuários (via safeApiCall): ${resource.message}")
                is Resource.Loading -> Log.d(TAG, "Recuperando a lista de usuários (via safeApiCall)...")
            }
        }
    }
}