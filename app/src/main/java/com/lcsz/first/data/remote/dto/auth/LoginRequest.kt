package com.lcsz.first.data.remote.dto.auth

data class LoginRequest(
    val cpf: String,
    val password: String
)