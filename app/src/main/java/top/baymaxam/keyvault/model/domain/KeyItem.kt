package top.baymaxam.keyvault.model.domain

import android.os.Parcelable
import top.baymaxam.keyvault.model.entity.KeyEntity
import java.util.Date

/**
 * 条目父类
 * @author John
 * @since 21 1月 2025
 */
sealed interface KeyItem : Parcelable {
    val id: String
    var name: String
    val createDate: Date
    var resentDate: Date
    var comment: String
}

fun KeyItem.asEntity(): KeyEntity {
    return when (this) {
        is UserItem -> this.asEntity()
        is AuthItem -> this.asEntity()
    }
}
