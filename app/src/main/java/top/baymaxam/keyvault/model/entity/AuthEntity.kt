package top.baymaxam.keyvault.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.benasher44.uuid.uuid4
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.domain.KeyType
import top.baymaxam.keyvault.util.DateConverter
import java.util.Date

/**
 * 授权实体
 * @author John
 * @since 30 6月 2024
 */
@Entity(tableName = "t_auth")
@TypeConverters(DateConverter::class)
data class AuthEntity(
    @PrimaryKey
    val id: String = uuid4().toString(),
    var name: String = "",
    var authorization: String = uuid4().toString(),
    var comment: String = "",
    var createDate: Date = Date(0),
    var lastUsedDate: Date = Date(0),
)

fun AuthEntity.asItem(transform: (String) -> KeyItem): KeyItem {
    return KeyItem(
        id = id,
        name = name,
        type = KeyType.Authorization,
        createDate = createDate,
        lastUsedDate = lastUsedDate,
        comment = comment,
        authorization = transform(authorization)
    )
}