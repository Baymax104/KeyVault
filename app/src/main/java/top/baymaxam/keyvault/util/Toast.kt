package top.baymaxam.keyvault.util

import com.blankj.utilcode.util.Utils
import es.dmoral.toasty.Toasty

/**
 * ToastUtil by Toasty
 * @author John
 * @since 2024/2/20
 */

fun successToast(message: String?) {
    Toasty.success(Utils.getApp(), message ?: "null").show()
}

fun errorToast(message: String?) {
    Toasty.error(Utils.getApp(), message ?: "null").show()
}

fun infoToast(message: String?) {
    Toasty.info(Utils.getApp(), message ?: "null").show()
}

