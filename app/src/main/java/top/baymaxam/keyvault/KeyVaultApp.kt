package top.baymaxam.keyvault

import android.app.Application
import com.blankj.utilcode.util.Utils
import es.dmoral.toasty.Toasty

/**
 * Application
 * @author John
 * @since 23 6月 2024
 */
class KeyVaultApp : Application() {
    override fun onCreate() {
        super.onCreate()

        Utils.init(this)

        Toasty.Config.getInstance()
            .setTextSize(14)
            .apply()
    }
}