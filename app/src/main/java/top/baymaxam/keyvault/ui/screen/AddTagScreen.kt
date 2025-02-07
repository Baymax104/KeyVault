package top.baymaxam.keyvault.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.bottomsheet.spec.DestinationStyleBottomSheet
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import top.baymaxam.keyvault.model.domain.Tag
import top.baymaxam.keyvault.state.TagListViewModel
import top.baymaxam.keyvault.ui.component.InputField
import top.baymaxam.keyvault.ui.component.TitleHeader
import top.baymaxam.keyvault.ui.theme.AppTheme
import top.baymaxam.keyvault.util.AddItemGraph
import top.baymaxam.keyvault.util.AddItemTagGraph
import top.baymaxam.keyvault.util.errorToast
import top.baymaxam.keyvault.util.successToast

/**
 * 添加标签页
 * @author John
 * @since 26 1月 2025
 */
@Destination<RootGraph>(style = DestinationStyleBottomSheet::class)
@Destination<AddItemGraph>
@Destination<AddItemTagGraph>
@Composable
fun AddTagScreen(navigator: DestinationsNavigator) {
    val vm = koinViewModel<TagListViewModel>()
    val nameState = remember { mutableStateOf("") }
    val nameErrorState = remember { mutableStateOf<Boolean>(false) }
    val scope = rememberCoroutineScope()
    ContentLayout(
        nameState = nameState,
        nameErrorState = nameErrorState,
        onBack = { navigator.navigateUp() },
        onConfirm = {
            scope.launch {
                val tag = Tag(name = nameState.value)
                vm.addTag(tag)
                    .onSuccess {
                        successToast("添加标签成功")
                        navigator.navigateUp()
                    }
                    .onFailure {
                        nameErrorState.value = it is IllegalArgumentException
                        errorToast(it.message)
                    }
            }
        }
    )
}

@Composable
private fun ContentLayout(
    nameState: MutableState<String> = mutableStateOf(""),
    nameErrorState: MutableState<Boolean> = mutableStateOf(false),
    onBack: () -> Unit = {},
    onConfirm: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.45f)
            .background(MaterialTheme.colorScheme.background)
    ) {
        TitleHeader(
            title = "新建标签",
            leadingIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Rounded.Close, contentDescription = null)
                }
            },
            trailingIcon = {
                IconButton(onClick = onConfirm) {
                    Icon(Icons.Rounded.Done, contentDescription = null)
                }
            }
        )
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            InputField(
                contentState = nameState,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .align(Alignment.Center),
                isError = nameErrorState.value,
                label = { Text("标签名称") },
                trailingIcon = {
                    if (nameState.value.isNotEmpty()) {
                        IconButton(onClick = {
                            nameState.value = ""
                            nameErrorState.value = false
                        }) {
                            Icon(imageVector = Icons.Rounded.Close, contentDescription = null)
                        }
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    AppTheme {
        ContentLayout()
    }
}
