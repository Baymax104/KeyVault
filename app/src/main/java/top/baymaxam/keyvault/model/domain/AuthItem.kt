package top.baymaxam.keyvault.model.domain

import android.os.Parcelable
import com.benasher44.uuid.uuid4
import kotlinx.parcelize.Parcelize
import top.baymaxam.keyvault.model.entity.KeyEntity
import java.util.Date

/**
 * 授权条目
 * @author John
 * @since 21 1月 2025
 */
@Parcelize
data class AuthItem(
    override val id: String = uuid4().toString(),
    override var name: String = "",
    override val createDate: Date = Date(0),
    override var resentDate: Date = Date(0),
    override var comment: String = "",
    val authId: String = "",
    var authName: String = ""
) : KeyItem, Parcelable


fun AuthItem.asEntity(): KeyEntity {
    return KeyEntity(
        id,
        name,
        KeyType.Authorization,
        authId = authId.ifEmpty { null },
        authName = authName.ifEmpty { null },
        createDate = createDate,
        resentDate = resentDate
    )
}
