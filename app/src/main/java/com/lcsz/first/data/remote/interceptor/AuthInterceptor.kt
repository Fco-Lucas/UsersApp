package com.lcsz.first.data.remote.interceptor

import com.lcsz.first.data.local.SessionManager
import okhttp3.Interceptor
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val sessionManager: SessionManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        // 1. Pega a requisição original
        val request = chain.request()

        // 2. Pega o token do SessionManager
        val token = sessionManager.fetchAuthToken()

        // 3. Se tivermos um token, adiciona o cabeçalho Authorization
        if (token != null) {
            val newRequest = request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            // 4. Prossegue com a NOVA requisição (com o cabeçalho)
            return chain.proceed(newRequest)
        }

        // 5. Se não tivermos token, prossegue com a requisição original (para endpoints públicos como /login)
        return chain.proceed(request)
    }
}