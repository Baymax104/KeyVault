package top.baymaxam.keyvault.model.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

/**
 * 条目数据类
 * @author John
 * @since 04 7月 2024
 */
@Parcelize
data class KeyItem(
    val id: Long = 0,
    var name: String = "",
    val type: KeyType = KeyType.Website,
    val createDate: Date = Date(),
    var lastUsedDate: Date = Date(),
    var username: String = "",
    var password: String = "",
    var comment: String = "",
    var authorization: KeyItem? = null,
    val tagIds: MutableList<Int> = mutableListOf()
) : Parcelable {
    init {
        if (authorization != null && authorization!!.type == KeyType.Authorization) {
            throw IllegalArgumentException("Authorization can not have authorization reference.")
        }
        if (type != KeyType.Authorization && authorization != null) {
            throw IllegalArgumentException("Non-authorization item can not have authorization reference.")
        }
    }
}