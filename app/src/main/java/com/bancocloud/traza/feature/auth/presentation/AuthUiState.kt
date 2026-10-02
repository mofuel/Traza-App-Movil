package com.bancocloud.traza.feature.auth.presentation

import com.bancocloud.traza.feature.auth.domain.AuthUser

sealed interface AuthUiState {
    data object Initializing : AuthUiState
    data object Unauthenticated : AuthUiState
    data object Authenticating : AuthUiState
    data class Authenticated(val user: AuthUser) : AuthUiState
    data class Error(val message: AuthMessage) : AuthUiState
}

enum class AuthMessage {
    INVALID_CREDENTIALS,
    NETWORK_UNAVAILABLE,
    SERVER_UNAVAILABLE,
    SESSION_EXPIRED,
    ACCESS_DENIED,
    ADMIN_ONLY,
    UNEXPECTED,
}
