package com.lcsz.first.data.remote.dto.users

data class UserCreateResponse(
    val id: String,
    val name: String,
    val cpf: String,
    val status: String
)