package top.baymaxam.keyvault.vm

import kotlinx.coroutines.flow.MutableStateFlow
import org.mindrot.jbcrypt.BCrypt
import top.baymaxam.keyvault.model.domain.DarkMode
import top.baymaxam.keyvault.model.domain.Expiry
import top.baymaxam.keyvault.repo.KVStore
import top.baymaxam.keyvault.repo.storeKey

/**
 * PreferenceViewModel
 * @author John
 * @since 10 2月 2025
 */
class PreferenceStateHolder(private val store: KVStore) {

    private val darkModeKey = storeKey<String>("dark_mode")
    private val authKey = storeKey<String>("auth")
    private val lastVerifyTimeKey = storeKey<Long>("last_verify_time")
    private val expiryKey = storeKey<String>("expiry_duration")

    val hasAuthorization: Boolean
        get() = authKey in store

    val isExpired: Boolean
        get() {
            val lastVerifyTime = store[lastVerifyTimeKey]
            val expiry = Expiry.valueOf(store[expiryKey]).value.inWholeMilliseconds
            return System.currentTimeMillis() - lastVerifyTime >= expiry
        }

    val darkModeFlow: MutableStateFlow<DarkMode> = MutableStateFlow(DarkMode.System)

    val expiryFlow: MutableStateFlow<Expiry> =
        MutableStateFlow(Expiry.ThirtyMinutes)

    init {
        store[darkModeKey] = DarkMode.System.name
        store[expiryKey] = Expiry.ThirtyMinutes.name
    }

    fun setAuthorization(password: String) {
        store[authKey] = BCrypt.hashpw(password, BCrypt.gensalt())
        store[lastVerifyTimeKey] = System.currentTimeMillis()
    }

    fun setExpiry(expiry: Expiry) {
        store[expiryKey] = expiry.name
        expiryFlow.value = expiry
    }

    fun setDarkMode(darkMode: DarkMode) {
        store[darkModeKey] = darkMode.name
        darkModeFlow.value = darkMode
    }


    fun verify(password: String): Result<Unit> {
        return runCatching {
            if (!BCrypt.checkpw(password, store[authKey])) {
                throw IllegalArgumentException("密钥错误")
            }
            store[lastVerifyTimeKey] = System.currentTimeMillis()
        }
    }
}