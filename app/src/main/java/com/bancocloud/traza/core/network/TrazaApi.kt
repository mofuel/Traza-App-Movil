package com.bancocloud.traza.core.network

import com.bancocloud.traza.core.network.dto.AuthTokenDto
import com.bancocloud.traza.core.network.dto.CurrentUserDto
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

internal const val PUBLIC_REQUEST_HEADER = "X-Traza-Public-Request"

interface TrazaApi {
    @FormUrlEncoded
    @Headers("$PUBLIC_REQUEST_HEADER: true")
    @POST("api/v1/auth/token")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String,
    ): AuthTokenDto

    @GET("api/v1/auth/me")
    suspend fun getCurrentUser(): CurrentUserDto
}
