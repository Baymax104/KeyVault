package top.baymaxam.keyvault.vm

import androidx.lifecycle.ViewModel
import org.mindrot.jbcrypt.BCrypt
import top.baymaxam.keyvault.model.domain.ExpiryDuration
import top.baymaxam.keyvault.repo.KVStore

/**
 * AuthManager
 * @author John
 * @since 07 2月 2025
 */
class AuthViewModel(private val store: KVStore) : ViewModel() {

    private val authKey = "auth"
    private val lastVerifyTimeKey = "last_verify_time"
    private val expiryTimeKey = "expiry_time"

    fun getAuthorization(): String? = store[authKey]

    fun setAuthorization(hash: String) = apply { store[authKey] = hash }

    fun getExpiryTime(): Long? = store[expiryTimeKey]

    fun setExpiryTime(time: Long) = apply { store[expiryTimeKey] = time }

    fun getLastVerifyTime(): Long? = store[lastVerifyTimeKey]

    fun setLastVerifyTime(time: Long) = apply { store[lastVerifyTimeKey] = time }

    fun isExpired(): Boolean {
        val expiryTime = getExpiryTime() ?: Long.MAX_VALUE
        val lastVerifyTime = getLastVerifyTime()!!
        val currentTime = System.currentTimeMillis()
        return currentTime - lastVerifyTime >= expiryTime
    }

    fun initAuthorization(password: String, expiry: ExpiryDuration): Result<Unit> {
        return runCatching {
            setAuthorization(BCrypt.hashpw(password, BCrypt.gensalt()))
            setLastVerifyTime(System.currentTimeMillis())
            setExpiryTime(expiry.value.inWholeMilliseconds)
        }
    }

    fun verify(password: String): Result<Unit> {
        return runCatching {
            val authorization = getAuthorization() ?: throw IllegalStateException("未设置密钥")
            if (!BCrypt.checkpw(password, authorization)) {
                throw IllegalArgumentException("密钥错误")
            }
            // success
            setLastVerifyTime(System.currentTimeMillis())
        }
    }
}
