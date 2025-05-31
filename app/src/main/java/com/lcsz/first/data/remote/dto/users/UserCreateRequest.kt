package com.lcsz.first.data.remote.dto.users

data class UserCreateRequest(
    val name: String,
    val cpf: String,
    val password: String
)