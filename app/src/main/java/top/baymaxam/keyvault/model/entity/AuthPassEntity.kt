package top.baymaxam.keyvault.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 授权实体
 * @author John
 * @since 30 6月 2024
 */
@Entity(tableName = "t_auth")
data class AuthPassEntity(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var name: String = "",
    var authorization: Long = 0,
    var createDate: String = "",
    var comment: String = "",
)