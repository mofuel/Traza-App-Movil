package com.bancocloud.traza.core.session

interface SessionStore {
    fun readToken(): String?
    fun writeToken(token: String)
    fun clearToken()
}
