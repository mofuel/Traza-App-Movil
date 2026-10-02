package com.bancocloud.traza.core.network

import com.bancocloud.traza.core.session.SessionManager
import com.bancocloud.traza.core.session.SessionStore
import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class BearerTokenInterceptorTest {
    private lateinit var server: MockWebServer

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()
    }

    @After
    fun tearDown() {
        server.close()
    }

    @Test
    fun `protected 401 clears session`() {
        val store = TestSessionStore("expired-token")
        val manager = SessionManager(store).also { it.restoreToken() }
        server.enqueue(MockResponse.Builder().code(401).build())

        execute(manager, Request.Builder().url(server.url("/api/v1/auth/me")).build())

        assertNull(store.token)
        assertNull(manager.currentToken())
        assertEquals("Bearer expired-token", server.takeRequest().headers["Authorization"])
    }

    @Test
    fun `protected 403 keeps session`() {
        val store = TestSessionStore("valid-token")
        val manager = SessionManager(store).also { it.restoreToken() }
        server.enqueue(MockResponse.Builder().code(403).build())

        execute(manager, Request.Builder().url(server.url("/api/v1/admin")).build())

        assertEquals("valid-token", store.token)
        assertEquals("valid-token", manager.currentToken())
    }

    @Test
    fun `public login never sends bearer token or internal marker`() {
        val store = TestSessionStore("existing-token")
        val manager = SessionManager(store).also { it.restoreToken() }
        server.enqueue(MockResponse.Builder().code(401).build())
        val request = Request.Builder()
            .url(server.url("/api/v1/auth/token"))
            .header(PUBLIC_REQUEST_HEADER, "true")
            .build()

        execute(manager, request)

        val recorded = server.takeRequest()
        assertNull(recorded.headers["Authorization"])
        assertNull(recorded.headers[PUBLIC_REQUEST_HEADER])
        assertEquals("existing-token", manager.currentToken())
    }

    private fun execute(manager: SessionManager, request: Request) {
        OkHttpClient.Builder()
            .addInterceptor(BearerTokenInterceptor(manager))
            .build()
            .newCall(request)
            .execute()
            .close()
    }
}

private class TestSessionStore(initialToken: String?) : SessionStore {
    var token: String? = initialToken

    override fun readToken(): String? = token
    override fun writeToken(token: String) {
        this.token = token
    }
    override fun clearToken() {
        token = null
    }
}
