package top.baymaxam.keyvault.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import top.baymaxam.keyvault.model.domain.PassType
import top.baymaxam.keyvault.util.DateConverter
import top.baymaxam.keyvault.util.PassTypeConverter
import java.util.Date

/**
 * 密码条目实体
 * @author John
 * @since 04 7月 2024
 */
@Entity(tableName = "t_password")
@TypeConverters(DateConverter::class, PassTypeConverter::class)
data class PasswordEntity(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var name: String = "",
    var username: String = "",
    var password: String = "",
    var createDate: Date = Date(),
    var comment: String = "",
    var lastUsedDate: Date = Date(),
    var type: PassType = PassType.Website
)
