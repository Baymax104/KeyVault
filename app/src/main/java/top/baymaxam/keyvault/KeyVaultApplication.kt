package top.baymaxam.keyvault

import android.app.Application
import com.blankj.utilcode.util.Utils
import es.dmoral.toasty.Toasty
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module
import top.baymaxam.keyvault.state.AddScreenModel
import top.baymaxam.keyvault.state.ItemListScreenModel

/**
 * Application
 * @author John
 * @since 23 6月 2024
 */
class KeyVaultApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Utils.init(this)

        Toasty.Config.getInstance()
            .setTextSize(14)
            .apply()

        startKoin {
            androidLogger()
            androidContext(this@KeyVaultApplication)
            modules(appModule)
        }
    }
}

val appModule = module {
    factory { AddScreenModel() }
    factory { ItemListScreenModel() }
}