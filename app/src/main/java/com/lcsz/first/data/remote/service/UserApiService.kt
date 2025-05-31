package com.lcsz.first.data.remote.service

import com.lcsz.first.data.remote.dto.users.UserCreateRequest
import com.lcsz.first.data.remote.dto.users.UserCreateResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApiService {
    @POST("users")
    suspend fun register(
        @Body request: UserCreateRequest
    ): Response<UserCreateResponse>
}