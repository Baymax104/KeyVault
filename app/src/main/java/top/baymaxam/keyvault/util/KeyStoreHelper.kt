package top.baymaxam.keyvault.util

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 * KeyStoreHelper
 * @author John
 * @since 07 2月 2025
 */
class KeyStoreHelper {
    private val keyAlias = "master_key"
    private val type = "AndroidKeyStore"
    private val cryptoMode = "AES/GCM/NoPadding"

    private fun getSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance(type).apply { load(null) }
        return if (keyStore.containsAlias(keyAlias)) {
            (keyStore.getEntry(keyAlias, null) as KeyStore.SecretKeyEntry).secretKey
        } else {
            createSecretKey()
        }
    }

    private fun createSecretKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, type)
        val purpose = KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        val keySpec = KeyGenParameterSpec.Builder(keyAlias, purpose)
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setRandomizedEncryptionRequired(false)
            .build()
        keyGenerator.init(keySpec)
        return keyGenerator.generateKey()
    }

    fun encrypt(password: String): CipherData {
        val secretKey = getSecretKey()
        val cipher = Cipher.getInstance(cryptoMode).apply { init(Cipher.ENCRYPT_MODE, secretKey) }
        val iv = cipher.iv
        val encrypted = cipher.doFinal(password.toByteArray())
        return CipherData(encrypted, iv)
    }

    fun decrypt(data: CipherData): String {
        val cipher = Cipher.getInstance(cryptoMode)
        val spec = GCMParameterSpec(128, data.iv)
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), spec)
        return String(cipher.doFinal(data.cipher))
    }
}

data class CipherData(
    val cipher: ByteArray,
    val iv: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CipherData) return false

        if (!cipher.contentEquals(other.cipher)) return false
        if (!iv.contentEquals(other.iv)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = cipher.contentHashCode()
        result = 31 * result + iv.contentHashCode()
        return result
    }
}