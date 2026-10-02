package com.bancocloud.traza.core.network

import com.bancocloud.traza.core.session.SessionManager
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object NetworkModule {
    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    fun createApi(sessionManager: SessionManager): TrazaApi {
        val client = OkHttpClient.Builder()
            .addInterceptor(BearerTokenInterceptor(sessionManager))
            .build()

        return Retrofit.Builder()
            .baseUrl(ApiConfiguration.baseUrl)
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(TrazaApi::class.java)
    }
}
