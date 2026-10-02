package com.bancocloud.traza.core.session

import java.util.concurrent.atomic.AtomicReference
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

sealed interface SessionEvent {
    data object Expired : SessionEvent
}

class SessionManager(
    private val store: SessionStore,
) {
    private val token = AtomicReference<String?>(null)
    private val mutableEvents = MutableSharedFlow<SessionEvent>(extraBufferCapacity = 1)
    val events = mutableEvents.asSharedFlow()

    fun restoreToken(): String? = store.readToken()
        ?.takeIf(String::isNotBlank)
        .also(token::set)

    fun saveToken(value: String) {
        require(value.isNotBlank()) { "The access token must not be blank." }
        store.writeToken(value)
        token.set(value)
    }

    fun currentToken(): String? = token.get()

    fun clearSession() {
        token.set(null)
        store.clearToken()
    }

    fun invalidateExpiredSession() {
        clearSession()
        mutableEvents.tryEmit(SessionEvent.Expired)
    }
}
