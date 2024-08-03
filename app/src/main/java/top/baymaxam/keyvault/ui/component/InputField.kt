package top.baymaxam.keyvault.ui.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import top.baymaxam.keyvault.ui.theme.AppTheme

/**
 * 搜索框
 * @author John
 * @since 27 6月 2024
 */

@Composable
fun SearchField(
    contentState: MutableState<String>,
    modifier: Modifier = Modifier,
    placeholder: @Composable () -> Unit = {},
    onSearch: () -> Unit = {}
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        value = contentState.value,
        onValueChange = { contentState.value = it },
        modifier = modifier,
        shape = RoundedCornerShape(50),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            unfocusedLeadingIconColor = Color.Black,
            focusedLeadingIconColor = MaterialTheme.colorScheme.primary
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onSearch()
                defaultKeyboardAction(ImeAction.Done)
            }
        ),
        placeholder = placeholder,
        leadingIcon = {
            IconButton(
                onClick = {
                    onSearch()
                    keyboardController?.hide()
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = null,
                )
            }
        },
        trailingIcon = {
            if (contentState.value.isNotEmpty()) {
                IconButton(onClick = { contentState.value = "" }) {
                    Icon(imageVector = Icons.Rounded.Close, contentDescription = null)
                }
            }
        }
    )
}

@Composable
fun InfoField(
    contentState: MutableState<String>,
    modifier: Modifier = Modifier,
    placeholder: @Composable () -> Unit = {},
    leadingIcon: @Composable () -> Unit = {},
) {
    OutlinedTextField(
        value = contentState.value,
        onValueChange = { contentState.value = it },
        modifier = modifier,
        shape = RoundedCornerShape(50),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            unfocusedLeadingIconColor = Color.Black,
            focusedLeadingIconColor = MaterialTheme.colorScheme.primary
        ),
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = {
            if (contentState.value.isNotEmpty()) {
                IconButton(onClick = { contentState.value = "" }) {
                    Icon(imageVector = Icons.Rounded.Close, contentDescription = null)
                }
            }
        }
    )
}


@Preview
@Composable
private fun Preview() {
    AppTheme {
        InfoField(remember { mutableStateOf("") })
    }
}

