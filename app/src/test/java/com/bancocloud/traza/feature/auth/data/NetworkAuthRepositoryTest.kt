package com.bancocloud.traza.feature.auth.data

import com.bancocloud.traza.core.network.TrazaApi
import com.bancocloud.traza.core.network.dto.AuthTokenDto
import com.bancocloud.traza.core.network.dto.CurrentUserDto
import com.bancocloud.traza.core.session.SessionManager
import com.bancocloud.traza.core.session.SessionStore
import com.bancocloud.traza.feature.auth.domain.AuthFailure
import com.bancocloud.traza.feature.auth.domain.AuthResult
import com.bancocloud.traza.feature.auth.domain.AuthRole
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class NetworkAuthRepositoryTest {
    @Test
    fun `successful ADMIN login stores token and returns user`() = runTest {
        val fixture = fixture(role = "ADMIN")

        val result = fixture.repository.login(" Admin@Example.com ", "")

        assertEquals(AuthRole.ADMIN, (result as AuthResult.Success).user.role)
        assertEquals("token-123", fixture.store.token)
        assertEquals("admin@example.com", fixture.api.lastUsername)
    }

    @Test
    fun `invalid credentials return expected failure and clear session`() = runTest {
        val fixture = fixture(loginFailure = httpException(401), storedToken = "old-token")

        val result = fixture.repository.login("admin@example.com", "")

        assertFailure(AuthFailure.INVALID_CREDENTIALS, result)
        assertNull(fixture.store.token)
    }

    @Test
    fun `current user with ADMIN role is accepted`() = runTest {
        val fixture = fixture(role = "ADMIN", storedToken = "valid-token")

        val result = fixture.repository.getCurrentUser()

        assertEquals(AuthRole.ADMIN, (result as AuthResult.Success).user.role)
        assertEquals("valid-token", fixture.store.token)
    }

    @Test
    fun `current user with CUSTOMER role is rejected and session cleared`() = runTest {
        val fixture = fixture(role = "CUSTOMER", storedToken = "customer-token")

        val result = fixture.repository.getCurrentUser()

        assertFailure(AuthFailure.ADMIN_ONLY, result)
        assertNull(fixture.store.token)
    }

    @Test
    fun `valid persisted session is restored through me`() = runTest {
        val fixture = fixture(role = "ADMIN", storedToken = "persisted-token")

        val result = fixture.repository.restoreSession()

        assertEquals("admin@example.com", (result as AuthResult.Success).user.email)
        assertEquals("persisted-token", fixture.sessionManager.currentToken())
    }

    @Test
    fun `invalid persisted session returns expired and clears token`() = runTest {
        val fixture = fixture(meFailure = httpException(401), storedToken = "expired-token")

        val result = fixture.repository.restoreSession()

        assertFailure(AuthFailure.SESSION_EXPIRED, result)
        assertNull(fixture.store.token)
        assertNull(fixture.sessionManager.currentToken())
    }

    @Test
    fun `401 from me after token exchange is session expired not invalid credentials`() = runTest {
        val fixture = fixture(meFailure = httpException(401))

        val result = fixture.repository.login("admin@example.com", "")

        assertFailure(AuthFailure.SESSION_EXPIRED, result)
        assertNull(fixture.store.token)
    }

    @Test
    fun `logout clears persisted and in-memory session`() = runTest {
        val fixture = fixture(storedToken = "valid-token")
        fixture.sessionManager.restoreToken()

        fixture.repository.logout()

        assertNull(fixture.store.token)
        assertNull(fixture.sessionManager.currentToken())
    }

    @Test
    fun `403 is access denied and does not invalidate session`() = runTest {
        val fixture = fixture(meFailure = httpException(403), storedToken = "valid-token")
        fixture.sessionManager.restoreToken()

        val result = fixture.repository.getCurrentUser()

        assertFailure(AuthFailure.ACCESS_DENIED, result)
        assertEquals("valid-token", fixture.store.token)
        assertEquals("valid-token", fixture.sessionManager.currentToken())
    }

    private fun fixture(
        role: String = "ADMIN",
        storedToken: String? = null,
        loginFailure: Exception? = null,
        meFailure: Exception? = null,
    ): Fixture {
        val store = InMemorySessionStore(storedToken)
        val sessionManager = SessionManager(store)
        val api = FakeTrazaApi(role, loginFailure, meFailure)
        val repository = NetworkAuthRepository(
            api = api,
            sessionManager = sessionManager,
            ioDispatcher = Dispatchers.Unconfined,
        )
        return Fixture(store, sessionManager, api, repository)
    }

    private fun assertFailure(expected: AuthFailure, result: AuthResult) {
        assertSame(expected, (result as AuthResult.Failure).reason)
    }

    private fun httpException(code: Int): HttpException = HttpException(
        Response.error<Unit>(code, "".toResponseBody()),
    )

    private data class Fixture(
        val store: InMemorySessionStore,
        val sessionManager: SessionManager,
        val api: FakeTrazaApi,
        val repository: NetworkAuthRepository,
    )
}

private class FakeTrazaApi(
    private val role: String,
    private val loginFailure: Exception?,
    private val meFailure: Exception?,
) : TrazaApi {
    var lastUsername: String? = null

    override suspend fun login(username: String, password: String): AuthTokenDto {
        lastUsername = username
        loginFailure?.let { throw it }
        return AuthTokenDto("token-123", "bearer", 1800)
    }

    override suspend fun getCurrentUser(): CurrentUserDto {
        meFailure?.let { throw it }
        return CurrentUserDto(
            id = "6f57cda1-d380-4f3c-aa47-0a745ada1923",
            email = "admin@example.com",
            role = role,
            customerId = null,
        )
    }
}

private class InMemorySessionStore(initialToken: String? = null) : SessionStore {
    var token: String? = initialToken

    override fun readToken(): String? = token

    override fun writeToken(token: String) {
        this.token = token
    }

    override fun clearToken() {
        token = null
    }
}
