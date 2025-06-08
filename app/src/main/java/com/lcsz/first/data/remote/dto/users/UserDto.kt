package com.lcsz.first.data.remote.dto.users

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserDto(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "cpf") val cpf: String,
    @Json(name = "status") val status: String
)