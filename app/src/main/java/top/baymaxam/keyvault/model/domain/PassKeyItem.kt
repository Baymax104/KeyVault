package top.baymaxam.keyvault.model.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

/**
 * 密码条目父类
 * @author John
 * @since 28 6月 2024
 */
@Parcelize
data class PassKeyItem(
    override val id: Long = 0,
    var name: String = "",
    val type: PassType = PassType.Website,
    val createDate: Date = Date(),
    var lastUsedDate: Date = Date(),
    var username: String = "",
    var password: String = "",
    var comment: String = ""
) : Parcelable, KeyItem