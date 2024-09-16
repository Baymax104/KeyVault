package top.baymaxam.keyvault.model.domain

/**
 * 密码条目类型
 * @author John
 * @since 04 7月 2024
 */
enum class KeyType(val intValue: Int) {
    Website(0),
    Card(1),
    Authorization(2)
}

fun String.toKeyType(): KeyType? {
    return when (this) {
        "Website" -> KeyType.Website
        "Card" -> KeyType.Card
        "Authorization" -> KeyType.Authorization
        else -> null
    }
}

fun Int.toKeyType(): KeyType? {
    return when (this) {
        0 -> KeyType.Website
        1 -> KeyType.Card
        2 -> KeyType.Authorization
        else -> null
    }
}