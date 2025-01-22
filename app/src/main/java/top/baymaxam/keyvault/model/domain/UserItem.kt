package top.baymaxam.keyvault.model.domain

import com.benasher44.uuid.uuid4
import kotlinx.parcelize.Parcelize
import top.baymaxam.keyvault.model.entity.KeyEntity
import java.util.Date

/**
 * 网站条目
 * @author John
 * @since 21 1月 2025
 */
@Parcelize
data class UserItem(
    override val id: String = uuid4().toString(),
    override var name: String = "",
    override val createDate: Date = Date(0),
    override var resentDate: Date = Date(0),
    override var comment: String = "",
    var username: String = "",
    var password: String = "",
) : KeyItem

fun UserItem.asEntity(): KeyEntity {
    return KeyEntity(
        id,
        name,
        KeyType.User,
        username,
        password,
        comment,
        createDate = createDate,
        resentDate = resentDate
    )
}
