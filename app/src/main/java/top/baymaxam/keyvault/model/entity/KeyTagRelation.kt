package top.baymaxam.keyvault.model.entity

import androidx.room.Entity

/**
 * Key、Tag关联表
 * @author John
 * @since 27 1月 2025
 */
@Entity(
    tableName = "t_key_tag",
    primaryKeys = ["keyId", "tagId"]
)
data class KeyTagRelation(
    val keyId: String,
    val tagId: String
)