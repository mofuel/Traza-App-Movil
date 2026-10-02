package com.bancocloud.traza.core.session

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.nio.charset.StandardCharsets
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class AndroidKeystoreSessionStore(context: Context) : SessionStore {
    private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    override fun readToken(): String? = runCatching {
        val encrypted = preferences.getString(KEY_CIPHERTEXT, null) ?: return null
        val initializationVector = preferences.getString(KEY_INITIALIZATION_VECTOR, null) ?: return null
        val cipher = Cipher.getInstance(TRANSFORMATION).apply {
            init(
                Cipher.DECRYPT_MODE,
                getOrCreateKey(),
                GCMParameterSpec(GCM_TAG_LENGTH_BITS, Base64.decode(initializationVector, Base64.NO_WRAP)),
            )
            updateAAD(ASSOCIATED_DATA)
        }
        String(
            cipher.doFinal(Base64.decode(encrypted, Base64.NO_WRAP)),
            StandardCharsets.UTF_8,
        )
    }.getOrElse {
        clearToken()
        null
    }

    override fun writeToken(token: String) {
        val cipher = Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.ENCRYPT_MODE, getOrCreateKey())
            updateAAD(ASSOCIATED_DATA)
        }
        val encrypted = cipher.doFinal(token.toByteArray(StandardCharsets.UTF_8))
        check(
            preferences.edit()
                .putString(KEY_CIPHERTEXT, Base64.encodeToString(encrypted, Base64.NO_WRAP))
                .putString(
                    KEY_INITIALIZATION_VECTOR,
                    Base64.encodeToString(cipher.iv, Base64.NO_WRAP),
                )
                .commit(),
        ) { "Unable to persist the encrypted session." }
    }

    override fun clearToken() {
        preferences.edit()
            .remove(KEY_CIPHERTEXT)
            .remove(KEY_INITIALIZATION_VECTOR)
            .commit()
    }

    private fun getOrCreateKey(): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE).apply { load(null) }
        (keyStore.getKey(KEY_ALIAS, null) as? SecretKey)?.let { return it }

        return KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE).run {
            init(
                KeyGenParameterSpec.Builder(
                    KEY_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT,
                )
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setRandomizedEncryptionRequired(true)
                    .build(),
            )
            generateKey()
        }
    }

    private companion object {
        const val PREFERENCES_NAME = "traza_secure_session"
        const val KEY_CIPHERTEXT = "access_token_ciphertext"
        const val KEY_INITIALIZATION_VECTOR = "access_token_iv"
        const val KEY_ALIAS = "traza_admin_session_key"
        const val ANDROID_KEY_STORE = "AndroidKeyStore"
        const val TRANSFORMATION = "AES/GCM/NoPadding"
        const val GCM_TAG_LENGTH_BITS = 128
        val ASSOCIATED_DATA = "traza-admin-session-v1".toByteArray(StandardCharsets.UTF_8)
    }
}
