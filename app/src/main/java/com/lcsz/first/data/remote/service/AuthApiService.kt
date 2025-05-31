package com.lcsz.first.data.remote.service

import com.lcsz.first.data.remote.dto.auth.LoginRequest
import com.lcsz.first.data.remote.dto.auth.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>
}