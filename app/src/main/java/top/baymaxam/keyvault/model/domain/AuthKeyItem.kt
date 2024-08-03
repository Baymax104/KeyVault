package top.baymaxam.keyvault.model.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

/**
 * 授权条目
 * @author John
 * @since 30 6月 2024
 */
@Parcelize
class AuthKeyItem(
    override val id: Long = 0,
    var name: String = "",
    var createDate: Date = Date(),
    var authorization: PassKeyItem = PassKeyItem(),
    var comment: String = "",
) : Parcelable, KeyItem