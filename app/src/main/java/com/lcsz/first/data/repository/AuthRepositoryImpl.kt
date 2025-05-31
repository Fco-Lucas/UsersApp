package com.lcsz.first.data.repository

import android.util.Log
import com.lcsz.first.data.remote.service.AuthApiService
import com.lcsz.first.data.remote.dto.auth.LoginRequest
import com.lcsz.first.data.remote.dto.auth.LoginResponse
import com.lcsz.first.data.remote.safeApiCall
import com.lcsz.first.domain.repository.AuthRepository
import com.lcsz.first.util.Resource
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

// Hilt injetará o ApiService aqui
class AuthRepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService,
    private val moshi: Moshi
) : AuthRepository { // Implementa a interface do domínio

    private val TAG = "AuthRepositoryImpl"

    override fun login(cpf: String, password: String): Flow<Resource<String>> { // Retorna Flow<Resource<String>> (o token)
        return safeApiCall(
            moshi = moshi,
            apiCall = { authApiService.login(LoginRequest(cpf = cpf, password = password)) }, // 1. A chamada real à API
            mapper = { loginResponseDto: LoginResponse -> // 2. Como mapear o DTO de sucesso para o que queremos
                loginResponseDto.token // Neste caso, queremos apenas o token da LoginResponse
            }
        ).onEach { resource -> // Opcional: Adicionar logs específicos para esta chamada, se necessário
            when (resource) {
                is Resource.Success -> Log.i(TAG, "Login para CPF $cpf bem-sucedido (via safeApiCall). Token: ${resource.data}")
                is Resource.Error -> Log.e(TAG, "Login para CPF $cpf falhou (via safeApiCall): ${resource.message}")
                is Resource.Loading -> Log.d(TAG, "Login para CPF $cpf carregando (via safeApiCall)...")
            }
        }
    }

//    override fun login(cpf: String, password: String): Flow<Resource<String>> = flow {
//        emit(Resource.Loading()) // Emite o estado de Loading primeiro
//
//        try {
//            val request = LoginRequest(cpf = cpf, password = password)
//            val response = authApiService.login(request) // Chamada à API
//
//            if (response.isSuccessful && response.body() != null) {
//                val loginResponseDto = response.body()!!
//                val token = loginResponseDto.token
//                Log.i(TAG, "Login bem-sucedido para $cpf, token recebido: $token")
//                emit(Resource.Success(token))
//            } else {
//                // Bloco de tratamento de erro atualizado abaixo
//                val errorBodyString = response.errorBody()?.string()
//                var specificErrorMessage: String? = null
//                if (!errorBodyString.isNullOrBlank()) {
//                    try {
//                        // Usa a instância do Moshi injetada para criar o adapter
//                        val adapter = moshi.adapter(ApiErrorResponse::class.java)
//                        val apiErrorResponse = adapter.fromJson(errorBodyString)
//                        specificErrorMessage = apiErrorResponse?.message
//                    } catch (e: Exception) {
//                        Log.e(TAG, "Falha ao analisar JSON da resposta de erro: $errorBodyString", e)
//                    }
//                }
//                // Se specificErrorMessage for nulo, usa uma mensagem de fallback com código/mensagem HTTP
//                val errorMsg = specificErrorMessage ?: "Erro: ${response.code()} (${response.message()})"
//                Log.e(TAG, "Erro no login para $cpf: ${response.code()} - $errorMsg")
//                emit(Resource.Error(errorMsg))
//            }
//        } catch (e: HttpException) { // Erro específico de HTTP (4xx, 5xx)
//            // Tenta analisar o corpo do erro da HttpException também, se possível
//            var httpErrorMessage: String? = null
//            val errorBody = e.response()?.errorBody()?.string()
//            if (!errorBody.isNullOrBlank()) {
//                try {
//                    val adapter = moshi.adapter(ApiErrorResponse::class.java)
//                    val apiErrorResponse = adapter.fromJson(errorBody)
//                    httpErrorMessage = apiErrorResponse?.message
//                } catch (jsonE: Exception) {
//                    Log.e(TAG, "Falha ao analisar JSON da HttpException: $errorBody", jsonE)
//                }
//            }
//            val finalHttpErrorMsg = httpErrorMessage ?: "Erro HTTP: ${e.code()} (${e.message()})"
//            Log.e(TAG, "Erro HTTP no login para $cpf: $finalHttpErrorMsg", e)
//            emit(Resource.Error(finalHttpErrorMsg))
//        } catch (e: IOException) { // Erro de rede (sem conexão, etc.)
//            Log.e(TAG, "Erro de rede no login para $cpf", e)
//            emit(Resource.Error("Não foi possível conectar ao servidor. Verifique sua conexão."))
//        } catch (e: Exception) { // Outros erros inesperados
//            Log.e(TAG, "Erro inesperado no login para $cpf", e)
//            emit(Resource.Error("Ocorreu um erro inesperado."))
//        }
//    }
}