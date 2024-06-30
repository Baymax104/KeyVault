package top.baymaxam.keyvault.model.domain

import java.util.Date

/**
 * 网站密码条目
 * @author John
 * @since 28 6月 2024
 */
class WebItem(
    id: Long = 0,
    name: String = "",
    username: String = "",
    password: String = "",
    createDate: Date = Date(),
    comment: String = "",
    var url: String = "",
) : PassItem(id, name, username, password, createDate, comment)