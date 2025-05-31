package com.lcsz.first.data.remote

import android.util.Log
import com.lcsz.first.data.remote.dto.ApiErrorResponse
import com.lcsz.first.util.Resource
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

// Função auxiliar para tentar analisar a resposta de erro customizada da API
fun parseCustomApiError(moshi: Moshi, errorBodyString: String?): String? {
    if (errorBodyString.isNullOrBlank()) {
        return null
    }
    return try {
        val adapter = moshi.adapter(ApiErrorResponse::class.java)
        adapter.fromJson(errorBodyString)?.message
    } catch (e: Exception) {
        Log.e("SafeApiCall", "Falha ao analisar JSON do corpo do erro customizado: '$errorBodyString'", e)
        null
    }
}

/**
 * Função genérica para fazer chamadas de API de forma segura, lidar com erros comuns
 * e mapear a resposta de sucesso.
 *
 * @param DTO O tipo do Data Transfer Object esperado da API em caso de sucesso.
 * @param Domain O tipo do Modelo de Domínio para o qual o DTO será mapeado em caso de sucesso.
 * @param moshi Instância do Moshi para analisar JSON de erro.
 * @param apiCall Lambda suspendida que executa a chamada real à API Retrofit e retorna um Response<DTO>.
 * @param mapper Lambda que converte o DTO de sucesso para o Modelo de Domínio.
 * @return Um Flow que emite estados Resource<Domain>.
 */
inline fun <DTO, Domain> safeApiCall(
    moshi: Moshi,
    crossinline apiCall: suspend () -> Response<DTO>,
    crossinline mapper: (dto: DTO) -> Domain
): Flow<Resource<Domain>> = flow {
    emit(Resource.Loading()) // Emitir estado de loading
    try {
        val response = apiCall() // Executa a chamada à API fornecida

        if (response.isSuccessful) {
            val body = response.body()

            if (body != null) {
                emit(Resource.Success(mapper(body)))
            } else {
                Log.w("SafeApiCall", "Resposta bem-sucedida mas com corpo nulo.")
                emit(Resource.Success(null))
            }
        } else { // Erro HTTP (não-2xx)
            val errorBody = response.errorBody()?.string()
            val customErrorMessage = parseCustomApiError(moshi, errorBody)
            val errorMessage = customErrorMessage ?: "Erro ${response.code()}: ${response.message()}"
            Log.e("SafeApiCall", "Erro na API: Código ${response.code()}, Corpo: '$errorBody', Mensagem final: '$errorMessage'")
            emit(Resource.Error(errorMessage))
        }
    } catch (e: HttpException) { // Erros HTTP que não foram pegos pelo 'else' acima (raro com Response<T>)
        val errorBody = e.response()?.errorBody()?.string()
        val customErrorMessage = parseCustomApiError(moshi, errorBody)
        val errorMessage = customErrorMessage ?: "Erro HTTP ${e.code()}: ${e.message()}"
        Log.e("SafeApiCall", "HttpException: Código ${e.code()}, Mensagem final: '$errorMessage'", e)
        emit(Resource.Error(errorMessage))
    } catch (e: IOException) { // Erros de rede (sem conexão, timeout, etc.)
        Log.e("SafeApiCall", "Erro de Rede (IOException)", e)
        emit(Resource.Error("Erro de rede. Verifique sua conexão ou tente novamente mais tarde."))
    } catch (e: Exception) { // Outros erros inesperados
        Log.e("SafeApiCall", "Erro inesperado na chamada à API", e)
        emit(Resource.Error("Ocorreu um erro inesperado: ${e.localizedMessage}"))
    }
}