package com.bancocloud.traza.feature.auth.domain

enum class AuthRole {
    ADMIN,
    CUSTOMER,
    UNKNOWN,
}

data class AuthUser(
    val id: String,
    val email: String,
    val role: AuthRole,
    val customerId: String?,
)

sealed interface AuthResult {
    data class Success(val user: AuthUser) : AuthResult
    data class Failure(val reason: AuthFailure) : AuthResult
}

enum class AuthFailure {
    NO_SESSION,
    INVALID_CREDENTIALS,
    NETWORK_UNAVAILABLE,
    SERVER_UNAVAILABLE,
    SESSION_EXPIRED,
    ACCESS_DENIED,
    ADMIN_ONLY,
    UNEXPECTED,
}
