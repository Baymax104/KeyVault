package top.baymaxam.keyvault.model.domain

/**
 * 密码条目类型
 * @author John
 * @since 04 7月 2024
 */
enum class KeyType {
    User,
    Authorization
}

fun String.toKeyType(): KeyType? {
    return when (this) {
        "User" -> KeyType.User
        "Authorization" -> KeyType.Authorization
        else -> null
    }
}
