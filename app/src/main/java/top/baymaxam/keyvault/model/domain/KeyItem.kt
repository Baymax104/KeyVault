package top.baymaxam.keyvault.model.domain

import android.os.Parcelable
import com.benasher44.uuid.uuid4
import kotlinx.parcelize.Parcelize
import top.baymaxam.keyvault.model.entity.AuthEntity
import top.baymaxam.keyvault.model.entity.PasswordEntity
import java.util.Date

/**
 * 条目数据类
 * @author John
 * @since 04 7月 2024
 */
@Parcelize
data class KeyItem(
    val id: String = uuid4().toString(),
    var name: String = "",
    val type: KeyType = KeyType.Website,
    val createDate: Date = Date(0),
    var lastUsedDate: Date = Date(0),
    var username: String = "",
    var password: String = "",
    var comment: String = "",
    var authorization: KeyItem? = null,
) : Parcelable {
    init {
        when (type) {
            KeyType.Authorization -> check(authorization?.type != KeyType.Authorization) {
                "Authorization can not have authorization reference."
            }

            else -> check(authorization == null) {
                "Non-authorization item can not have authorization reference."
            }
        }
    }
}

fun KeyItem.asPasswordEntity(): PasswordEntity {
    check(type != KeyType.Authorization) {
        "Authorization item can not be converted to PasswordEntity."
    }
    return PasswordEntity(
        id = id,
        name = name,
        type = type,
        username = username,
        password = password,
        comment = comment,
        createDate = createDate,
        lastUsedDate = lastUsedDate,
    )
}

fun KeyItem.asAuthEntity(): AuthEntity {
    check(type == KeyType.Authorization) {
        "Non-authorization item can not be converted to AuthEntity."
    }
    return AuthEntity(
        id = id,
        name = name,
        authorization = authorization!!.id,
        comment = comment,
        createDate = createDate,
        lastUsedDate = lastUsedDate
    )
}