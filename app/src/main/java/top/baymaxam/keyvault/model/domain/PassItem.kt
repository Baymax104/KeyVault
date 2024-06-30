package top.baymaxam.keyvault.model.domain

import java.util.Date

/**
 * 密码条目父类
 * @author John
 * @since 28 6月 2024
 */
abstract class PassItem(
    val id: Long = 0,
    var name: String = "",
    var username: String = "",
    var password: String = "",
    val createDate: Date = Date(),
    var comment: String = "",
)