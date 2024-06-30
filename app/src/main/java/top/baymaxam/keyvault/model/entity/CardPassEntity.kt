package top.baymaxam.keyvault.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * 卡片密码实体
 * @author John
 * @since 30 6月 2024
 */
@Entity(tableName = "t_card")
data class CardPassEntity(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var name: String = "",
    var username: String = "",
    var password: String = "",
    var createDate: String = "",
    var comment: String = ""
)
