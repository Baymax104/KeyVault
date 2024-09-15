package top.baymaxam.keyvault.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.benasher44.uuid.uuid4
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.domain.KeyType
import top.baymaxam.keyvault.util.DateConverter
import top.baymaxam.keyvault.util.KeyTypeConverter
import java.util.Date

/**
 * 密码条目实体
 * @author John
 * @since 04 7月 2024
 */
@Entity(tableName = "t_password")
@TypeConverters(DateConverter::class, KeyTypeConverter::class)
data class PasswordEntity(
    @PrimaryKey
    val id: String = uuid4().toString(),
    var name: String = "",
    var username: String = "",
    var password: String = "",
    var createDate: Date = Date(0),
    var comment: String = "",
    var lastUsedDate: Date = Date(0),
    var type: KeyType = KeyType.Website
)

fun PasswordEntity.asItem(): KeyItem {
    return KeyItem(
        id = id,
        name = name,
        type = type,
        username = username,
        password = password,
        comment = comment,
        createDate = createDate,
        lastUsedDate = lastUsedDate
    )
}
