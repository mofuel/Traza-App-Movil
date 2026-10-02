package com.bancocloud.traza.feature.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bancocloud.traza.core.session.SessionEvent
import com.bancocloud.traza.core.session.SessionManager
import com.bancocloud.traza.feature.auth.data.AuthRepository
import com.bancocloud.traza.feature.auth.domain.AuthFailure
import com.bancocloud.traza.feature.auth.domain.AuthResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository,
    sessionManager: SessionManager,
) : ViewModel() {
    private val mutableUiState = MutableStateFlow<AuthUiState>(AuthUiState.Initializing)
    val uiState = mutableUiState.asStateFlow()

    init {
        viewModelScope.launch {
            sessionManager.events.collect { event ->
                if (event == SessionEvent.Expired) {
                    mutableUiState.value = AuthUiState.Error(AuthMessage.SESSION_EXPIRED)
                }
            }
        }
        restoreSession()
    }

    fun login(email: String, password: String) {
        if (mutableUiState.value == AuthUiState.Authenticating) return
        viewModelScope.launch {
            mutableUiState.value = AuthUiState.Authenticating
            mutableUiState.value = repository.login(email, password).toUiState()
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            mutableUiState.value = AuthUiState.Unauthenticated
        }
    }

    private fun restoreSession() {
        viewModelScope.launch {
            mutableUiState.value = repository.restoreSession().toUiState()
        }
    }

    companion object {
        fun factory(repository: AuthRepository, sessionManager: SessionManager): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    require(modelClass.isAssignableFrom(AuthViewModel::class.java))
                    return AuthViewModel(repository, sessionManager) as T
                }
            }
    }
}

private fun AuthResult.toUiState(): AuthUiState = when (this) {
    is AuthResult.Success -> AuthUiState.Authenticated(user)
    is AuthResult.Failure -> when (reason) {
        AuthFailure.NO_SESSION -> AuthUiState.Unauthenticated
        else -> AuthUiState.Error(reason.toMessage())
    }
}

private fun AuthFailure.toMessage(): AuthMessage = when (this) {
    AuthFailure.INVALID_CREDENTIALS -> AuthMessage.INVALID_CREDENTIALS
    AuthFailure.NETWORK_UNAVAILABLE -> AuthMessage.NETWORK_UNAVAILABLE
    AuthFailure.SERVER_UNAVAILABLE -> AuthMessage.SERVER_UNAVAILABLE
    AuthFailure.SESSION_EXPIRED -> AuthMessage.SESSION_EXPIRED
    AuthFailure.ACCESS_DENIED -> AuthMessage.ACCESS_DENIED
    AuthFailure.ADMIN_ONLY -> AuthMessage.ADMIN_ONLY
    AuthFailure.UNEXPECTED, AuthFailure.NO_SESSION -> AuthMessage.UNEXPECTED
}
