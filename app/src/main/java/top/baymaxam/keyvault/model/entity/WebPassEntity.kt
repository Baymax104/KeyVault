package top.baymaxam.keyvault.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * 网站密码条目实体
 * @author John
 * @since 30 6月 2024
 */
@Entity(tableName = "t_web")
data class WebPassEntity(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var name: String = "",
    var password: String = "",
    var createDate: String = "",
    var comment: String = "",
    var url: String = ""
)
