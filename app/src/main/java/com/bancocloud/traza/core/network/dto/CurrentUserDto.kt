package com.bancocloud.traza.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentUserDto(
    val id: String,
    val email: String,
    val role: String,
    @SerialName("customer_id") val customerId: String?,
)
