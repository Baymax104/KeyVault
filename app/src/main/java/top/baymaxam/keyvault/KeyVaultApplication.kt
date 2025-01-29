package top.baymaxam.keyvault

import android.app.Application
import com.blankj.utilcode.util.Utils
import es.dmoral.toasty.Toasty
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.repo.KeyRepository
import top.baymaxam.keyvault.repo.LocalDatabase
import top.baymaxam.keyvault.repo.TagRepository
import top.baymaxam.keyvault.state.AddAuthViewModel
import top.baymaxam.keyvault.state.AddItemTagViewModel
import top.baymaxam.keyvault.state.AddItemViewModel
import top.baymaxam.keyvault.state.ItemListViewModel
import top.baymaxam.keyvault.state.ItemViewModel
import top.baymaxam.keyvault.state.TagListViewModel

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
    single { LocalDatabase.Instance.keyDao() }
    single { LocalDatabase.Instance.tagDao() }
    single { LocalDatabase.Instance.keyTagDao() }
    single { TagRepository(get(), get()) }
    single { KeyRepository(get(), get()) }
    viewModel { ItemListViewModel(get()) }
    viewModel { (item: KeyItem) -> ItemViewModel(get(), get(), item) }
    viewModel { AddItemViewModel(get(), get()) }
    viewModel { AddAuthViewModel(get()) }
    viewModel { (item: KeyItem) -> AddItemTagViewModel(get(), item) }
    viewModel { TagListViewModel(get()) }
}