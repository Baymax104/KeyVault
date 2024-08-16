package top.baymaxam.keyvault.util

import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.PermissionUtils.SimpleCallback

/**
 * 权限申请回调
 * @constructor Create empty Permission callback
 */
class PermissionCallback : SimpleCallback {
    var granted: () -> Unit = {}
    var denied: () -> Unit = {}

    companion object {
        inline fun build(block: PermissionCallback.() -> Unit) =
            PermissionCallback().apply(block)
    }

    fun granted(action: () -> Unit) = apply { granted = action }
    fun denied(action: () -> Unit) = apply { denied = action }

    override fun onGranted() = granted()
    override fun onDenied() = denied()
}

/**
 * Request permission
 *
 * @param permission 权限名
 * @param block Permission Builder
 * @receiver [PermissionCallback]
 */
inline fun requestPermission(permission: String, block: PermissionCallback.() -> Unit) {
    val callback = PermissionCallback.build(block)
    if (PermissionUtils.isGranted(permission)) {
        callback.granted()
    } else {
        PermissionUtils.permission(permission).callback(callback).request()
    }
}