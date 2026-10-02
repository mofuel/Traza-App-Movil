package com.bancocloud.traza.feature.auth.data

import com.bancocloud.traza.feature.auth.domain.AuthResult

interface AuthRepository {
    suspend fun login(email: String, password: String): AuthResult
    suspend fun getCurrentUser(): AuthResult
    suspend fun restoreSession(): AuthResult
    suspend fun logout()
}
