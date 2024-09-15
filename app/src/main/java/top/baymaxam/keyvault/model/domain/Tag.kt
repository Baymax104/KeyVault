package top.baymaxam.keyvault.model.domain

import android.os.Parcelable
import com.benasher44.uuid.uuid4
import kotlinx.parcelize.Parcelize

/**
 * 标签
 * @author John
 * @since 04 7月 2024
 */
@Parcelize
data class Tag(
    val id: String = uuid4().toString(),
    var name: String = ""
) : Parcelable
