package top.baymaxam.keyvault.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.benasher44.uuid.uuid4
import top.baymaxam.keyvault.model.domain.Tag

/**
 * 标签实体
 * @author John
 * @since 04 7月 2024
 */
@Entity(tableName = "t_tag")
data class TagEntity(
    @PrimaryKey
    val id: String = uuid4().toString(),
    var name: String = ""
)

fun TagEntity.asItem(): Tag = Tag(id, name)
