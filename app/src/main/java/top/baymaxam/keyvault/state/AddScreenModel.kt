package top.baymaxam.keyvault.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import top.baymaxam.keyvault.model.domain.Tag

/**
 * AddScreenViewModel
 * @author John
 * @since 01 8月 2024
 */
@Stable
class AddScreenModel : ScreenModel {

    val searchState: MutableState<String> = mutableStateOf("")
    val tags = mutableStateListOf<TagItemState>()
    val tagsCache = mutableListOf<TagItemState>()

    val nameState: MutableState<String> = mutableStateOf("")
    val usernameState: MutableState<String> = mutableStateOf("")
    val passwordState: MutableState<String> = mutableStateOf("")
    val commentState: MutableState<String> = mutableStateOf("")

    init {
        tags.addAll(
            listOf(
                TagItemState(Tag(id = 0, name = "Hello")),
                TagItemState(Tag(id = 1, name = "Hello1")),
                TagItemState(Tag(id = 2, name = "Hello2")),
                TagItemState(Tag(id = 3, name = "Hello3")),
                TagItemState(Tag(id = 4, name = "Hello4")),
            )
        )
        tagsCache.addAll(tags)


    }

    fun searchTag() {
        tags.filter {
            it.value.name.contains(searchState.value, true)
        }.let {
            tags.clear()
            tags.addAll(it)
        }

    }

    fun refreshTags() {
        tags.clear()
        tags.addAll(tagsCache)
    }
}