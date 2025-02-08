package top.baymaxam.keyvault.repo

import com.tencent.mmkv.MMKV

/**
 * DataStoreHelper
 * @author John
 * @since 07 2月 2025
 */
class KVStore {

    val mmkv = MMKV.defaultMMKV()

    operator fun <T> set(key: String, value: T): Unit = with(mmkv) {
        when (value) {
            is String -> encode(key, value)
            is Int -> encode(key, value)
            is Float -> encode(key, value)
            is Double -> encode(key, value)
            is Long -> encode(key, value)
            is Boolean -> encode(key, value)
            is ByteArray -> encode(key, value)
        }
    }

    inline operator fun <reified T> get(key: String): T? = with(mmkv) {
        when (T::class) {
            String::class -> decodeString(key)
            Int::class -> decodeInt(key)
            Float::class -> decodeFloat(key)
            Double::class -> decodeDouble(key)
            Boolean::class -> decodeBool(key)
            Long::class -> decodeLong(key)
            ByteArray::class -> decodeBytes(key)
            else -> null
        } as? T
    }

    operator fun minusAssign(key: String): Unit = mmkv.removeValueForKey(key)

    operator fun contains(key: String): Boolean = mmkv.containsKey(key)

}