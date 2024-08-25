package top.baymaxam.keyvault.model.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 标签
 * @author John
 * @since 04 7月 2024
 */
@Parcelize
data class Tag(
    val id: Long = 0,
    var name: String = ""
) : Parcelable
