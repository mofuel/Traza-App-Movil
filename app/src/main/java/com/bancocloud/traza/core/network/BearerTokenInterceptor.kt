package com.bancocloud.traza.core.network

import com.bancocloud.traza.core.session.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

class BearerTokenInterceptor(
    private val sessionManager: SessionManager,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val isPublic = original.header(PUBLIC_REQUEST_HEADER) != null
        val requestBuilder = original.newBuilder().removeHeader(PUBLIC_REQUEST_HEADER)

        if (!isPublic) {
            sessionManager.currentToken()?.let { token ->
                requestBuilder.header("Authorization", "Bearer $token")
            }
        }

        val response = chain.proceed(requestBuilder.build())
        if (!isPublic && response.code == 401) {
            sessionManager.invalidateExpiredSession()
        }
        return response
    }
}
