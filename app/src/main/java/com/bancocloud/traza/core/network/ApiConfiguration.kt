package com.bancocloud.traza.core.network

import com.bancocloud.traza.BuildConfig

object ApiConfiguration {
    val baseUrl: String = BuildConfig.TRAZA_API_BASE_URL.also { value ->
        require(value.endsWith('/')) { "The API base URL must end with '/'." }
        require(value.startsWith("http://") || value.startsWith("https://")) {
            "The API base URL must use HTTP or HTTPS."
        }
    }
}
