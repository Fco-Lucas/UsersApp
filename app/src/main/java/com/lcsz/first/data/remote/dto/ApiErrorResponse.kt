package com.lcsz.first.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiErrorResponse( // Transformado em data class com campos no construtor
    @Json(name = "path")
    val path: String?, // Mantidos como nuláveis para flexibilidade
    @Json(name = "method")
    val method: String?,
    @Json(name = "status")
    val status: Int?, // Alterado para Int?, pois 400 é um inteiro
    @Json(name = "statusText")
    val statusText: String?,
    @Json(name = "message")
    val message: String? // O campo crucial que queremos
)