package com.bancocloud.traza.feature.auth.data

import com.bancocloud.traza.core.network.TrazaApi
import com.bancocloud.traza.core.network.dto.CurrentUserDto
import com.bancocloud.traza.core.session.SessionManager
import com.bancocloud.traza.feature.auth.domain.AuthFailure
import com.bancocloud.traza.feature.auth.domain.AuthResult
import com.bancocloud.traza.feature.auth.domain.AuthRole
import com.bancocloud.traza.feature.auth.domain.AuthUser
import java.io.IOException
import java.util.Locale
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class NetworkAuthRepository(
    private val api: TrazaApi,
    private val sessionManager: SessionManager,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : AuthRepository {
    override suspend fun login(email: String, password: String): AuthResult = withContext(ioDispatcher) {
        sessionManager.clearSession()
        val token = try {
            api.login(email.trim().lowercase(Locale.ROOT), password)
        } catch (exception: Exception) {
            if (exception is CancellationException) throw exception
            return@withContext AuthResult.Failure(mapLoginFailure(exception))
        }

        try {
            if (!token.tokenType.equals("bearer", ignoreCase = true) || token.accessToken.isBlank()) {
                return@withContext AuthResult.Failure(AuthFailure.UNEXPECTED)
            }
            sessionManager.saveToken(token.accessToken)
        } catch (exception: Exception) {
            if (exception is CancellationException) throw exception
            sessionManager.clearSession()
            return@withContext AuthResult.Failure(AuthFailure.UNEXPECTED)
        }

        protectedCurrentUserResult()
    }

    override suspend fun getCurrentUser(): AuthResult = withContext(ioDispatcher) {
        protectedCurrentUserResult()
    }

    override suspend fun restoreSession(): AuthResult = withContext(ioDispatcher) {
        if (sessionManager.restoreToken() == null) {
            return@withContext AuthResult.Failure(AuthFailure.NO_SESSION)
        }
        protectedCurrentUserResult()
    }

    override suspend fun logout() = withContext(ioDispatcher) {
        sessionManager.clearSession()
    }

    private suspend fun currentUserResult(): AuthResult {
        val user = api.getCurrentUser().toDomain()
        if (user.role != AuthRole.ADMIN) {
            sessionManager.clearSession()
            return AuthResult.Failure(AuthFailure.ADMIN_ONLY)
        }
        return AuthResult.Success(user)
    }

    private suspend fun protectedCurrentUserResult(): AuthResult = try {
        currentUserResult()
    } catch (exception: Exception) {
        if (exception is CancellationException) throw exception
        AuthResult.Failure(mapProtectedFailure(exception))
    }

    private fun mapLoginFailure(exception: Exception): AuthFailure = when (exception) {
        is IOException -> AuthFailure.NETWORK_UNAVAILABLE
        is HttpException -> when {
            exception.code() == 401 -> AuthFailure.INVALID_CREDENTIALS
            exception.code() in 500..599 -> AuthFailure.SERVER_UNAVAILABLE
            else -> AuthFailure.UNEXPECTED
        }
        else -> AuthFailure.UNEXPECTED
    }

    private fun mapProtectedFailure(exception: Exception): AuthFailure = when (exception) {
        is IOException -> AuthFailure.NETWORK_UNAVAILABLE
        is HttpException -> when {
            exception.code() == 401 -> {
                sessionManager.clearSession()
                AuthFailure.SESSION_EXPIRED
            }
            exception.code() == 403 -> AuthFailure.ACCESS_DENIED
            exception.code() in 500..599 -> AuthFailure.SERVER_UNAVAILABLE
            else -> AuthFailure.UNEXPECTED
        }
        else -> AuthFailure.UNEXPECTED
    }
}

private fun CurrentUserDto.toDomain(): AuthUser = AuthUser(
    id = id,
    email = email,
    role = when (role.uppercase()) {
        "ADMIN" -> AuthRole.ADMIN
        "CUSTOMER" -> AuthRole.CUSTOMER
        else -> AuthRole.UNKNOWN
    },
    customerId = customerId,
)
