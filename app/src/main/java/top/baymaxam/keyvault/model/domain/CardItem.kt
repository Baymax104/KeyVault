package top.baymaxam.keyvault.model.domain

import java.util.Date

/**
 * 卡片密码条目
 * @author John
 * @since 28 6月 2024
 */
class CardItem(
    id: Long = 0,
    name: String = "",
    username: String = "",
    password: String = "",
    createDate: Date = Date(),
    comment: String = "",
) : PassItem(id, name, username, password, createDate, comment)