package top.baymaxam.keyvault.model.domain

import android.os.Parcelable
import com.benasher44.uuid.uuid4
import kotlinx.parcelize.Parcelize
import top.baymaxam.keyvault.model.entity.KeyEntity
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
    var authId: String? = null,
    var authName: String? = null
) : Parcelable {
    init {
        if (type == KeyType.Authorization) {
            check(authId != null && authName != null) { "Authorization's reference is null." }
        }
    }
}

fun KeyItem.asEntity(): KeyEntity =
    KeyEntity(
        id = id,
        name = name,
        type = type,
        username = username,
        password = password,
        comment = comment,
        authId = authId,
        authName = authName,
        createDate = createDate,
        lastUsedDate = lastUsedDate
    )
