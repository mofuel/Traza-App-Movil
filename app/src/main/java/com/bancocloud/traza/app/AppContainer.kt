package com.bancocloud.traza.app

import android.content.Context
import com.bancocloud.traza.core.network.NetworkModule
import com.bancocloud.traza.core.session.AndroidKeystoreSessionStore
import com.bancocloud.traza.core.session.SessionManager
import com.bancocloud.traza.feature.auth.data.AuthRepository
import com.bancocloud.traza.feature.auth.data.NetworkAuthRepository

class AppContainer(context: Context) {
    val sessionManager = SessionManager(AndroidKeystoreSessionStore(context))
    val authRepository: AuthRepository = NetworkAuthRepository(
        api = NetworkModule.createApi(sessionManager),
        sessionManager = sessionManager,
    )
}
