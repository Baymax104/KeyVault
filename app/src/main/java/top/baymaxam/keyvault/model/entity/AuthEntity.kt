package top.baymaxam.keyvault.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
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
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var name: String = "",
    var authorization: Long = 0,
    var createDate: Date = Date(0),
    var comment: String = "",
    var lastUsedDate: Date = Date(0),
)