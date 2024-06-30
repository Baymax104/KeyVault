package top.baymaxam.keyvault.model.domain

import java.util.Date

/**
 * 授权条目
 * @author John
 * @since 30 6月 2024
 */
class AuthItem(
    id: Long = 0,
    name: String = "",
    createDate: Date = Date(),
    comment: String = "",
    var authorization: WebItem = WebItem(),
) : PassItem(id, name, createDate = createDate, comment = comment)