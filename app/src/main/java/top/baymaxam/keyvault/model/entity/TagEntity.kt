package top.baymaxam.keyvault.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 标签实体
 * @author John
 * @since 04 7月 2024
 */
@Entity(tableName = "t_tag")
data class TagEntity(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var name: String = ""
)
