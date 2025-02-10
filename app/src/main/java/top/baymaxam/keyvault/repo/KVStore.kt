package top.baymaxam.keyvault.repo

import com.tencent.mmkv.MMKV

/**
 * DataStoreHelper
 * @author John
 * @since 07 2月 2025
 */
class KVStore {

    val mmkv = MMKV.defaultMMKV()

    @Suppress("Unused")
    data class Key<T>(val name: String)

    operator fun <T> set(key: Key<T>, value: T): Unit = with(mmkv) {
        val (name) = key
        when (value) {
            is String -> encode(name, value)
            is Int -> encode(name, value)
            is Float -> encode(name, value)
            is Double -> encode(name, value)
            is Long -> encode(name, value)
            is Boolean -> encode(name, value)
            is ByteArray -> encode(name, value)
        }
    }

    inline operator fun <reified T> get(key: Key<T>): T {
        if (key !in this) {
            throw NoSuchElementException("No such key")
        }
        val (name) = key
        return with(mmkv) {
            when (T::class) {
                String::class -> decodeString(name)
                Int::class -> decodeInt(name)
                Float::class -> decodeFloat(name)
                Double::class -> decodeDouble(name)
                Boolean::class -> decodeBool(name)
                Long::class -> decodeLong(name)
                ByteArray::class -> decodeBytes(name)
                else -> throw UnsupportedOperationException("Unsupported Type")
            } as T
        }
    }

    operator fun minusAssign(key: Key<*>): Unit = mmkv.removeValueForKey(key.name)

    operator fun contains(key: Key<*>): Boolean = mmkv.containsKey(key.name)

}

fun <T> storeKey(name: String) = KVStore.Key<T>(name)
