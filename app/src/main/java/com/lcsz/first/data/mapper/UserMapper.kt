package com.lcsz.first.data.mapper

import com.lcsz.first.data.remote.dto.users.UserCreateResponse
import com.lcsz.first.domain.model.User

// Função de extensão para mapear UserCreateResponse (DTO) para User (Domain Model)
fun UserCreateResponse.toDomainUser(): User {
    return User(
        id = this.id,
        name = this.name,
        cpf = this.cpf,
        status = this.status,
    )
}