package com.lcsz.first.data.mapper

import com.lcsz.first.data.remote.dto.users.UserDto
import com.lcsz.first.domain.model.User

// Função de extensão para mapear UserDto (DTO) para User (Domain Model)
fun UserDto.toDomainUser(): User {
    return User(
        id = this.id,
        name = this.name,
        cpf = this.cpf,
        status = this.status,
    )
}