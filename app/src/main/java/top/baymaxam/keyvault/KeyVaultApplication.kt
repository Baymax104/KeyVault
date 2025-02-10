package top.baymaxam.keyvault

import android.app.Application
import com.blankj.utilcode.util.Utils
import com.tencent.mmkv.MMKV
import es.dmoral.toasty.Toasty
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.domain.Tag
import top.baymaxam.keyvault.repo.KVStore
import top.baymaxam.keyvault.repo.KeyRepository
import top.baymaxam.keyvault.repo.LocalDatabase
import top.baymaxam.keyvault.repo.TagRepository
import top.baymaxam.keyvault.vm.AddItemTagViewModel
import top.baymaxam.keyvault.vm.AddItemViewModel
import top.baymaxam.keyvault.vm.ItemListViewModel
import top.baymaxam.keyvault.vm.ItemViewModel
import top.baymaxam.keyvault.vm.PreferenceStateHolder
import top.baymaxam.keyvault.vm.SelectItemViewModel
import top.baymaxam.keyvault.vm.SelectUserItemViewModel
import top.baymaxam.keyvault.vm.TagItemListViewModel
import top.baymaxam.keyvault.vm.TagListViewModel

/**
 * Application
 * @author John
 * @since 23 6月 2024
 */
class KeyVaultApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Utils.init(this)

        MMKV.initialize(this)

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
    single { KVStore() }
    single { LocalDatabase.Instance.keyDao() }
    single { LocalDatabase.Instance.tagDao() }
    single { LocalDatabase.Instance.keyTagDao() }
    single { TagRepository(get(), get()) }
    single { KeyRepository(get(), get()) }
    single { PreferenceStateHolder(get()) }
    viewModel { ItemListViewModel(get()) }
    viewModel { (item: KeyItem) -> ItemViewModel(get(), get(), item) }
    viewModel { AddItemViewModel(get(), get()) }
    viewModel { SelectUserItemViewModel(get()) }
    viewModel { (item: KeyItem) -> AddItemTagViewModel(get(), item) }
    viewModel { (tag: Tag) -> SelectItemViewModel(get(), tag) }
    viewModel { TagListViewModel(get()) }
    viewModel { (tag: Tag) -> TagItemListViewModel(get(), tag) }
}