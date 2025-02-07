package top.baymaxam.keyvault.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import top.baymaxam.keyvault.ui.theme.AppTheme
import top.baymaxam.keyvault.ui.theme.outlinedTextFieldColor

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
    if (contentState.value.isNotEmpty()) {
        onSearch()
    }
    OutlinedTextField(
        value = contentState.value,
        onValueChange = { contentState.value = it },
        modifier = modifier.defaultMinSize(
            minWidth = InputFieldDefaults.MinWidth,
            minHeight = InputFieldDefaults.MinHeight
        ),
        shape = RoundedCornerShape(50),
        singleLine = true,
        colors = MaterialTheme.outlinedTextFieldColor,
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
fun InputField(
    contentState: MutableState<String>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,
    shape: Shape = InputFieldDefaults.shape,
    label: (@Composable () -> Unit)? = null,
    placeholder: (@Composable () -> Unit)? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
) {
    OutlinedTextField(
        value = contentState.value,
        onValueChange = { contentState.value = it },
        enabled = enabled,
        isError = isError,
        shape = shape,
        singleLine = true,
        colors = MaterialTheme.outlinedTextFieldColor,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        modifier = modifier.defaultMinSize(
            minWidth = InputFieldDefaults.MinWidth,
            minHeight = InputFieldDefaults.MinHeight
        )
    )
}


@Composable
fun CommentField(
    contentState: MutableState<String>,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.defaultMinSize(minHeight = 60.dp)
    ) {
        OutlinedTextField(
            value = contentState.value,
            onValueChange = { contentState.value = it },
            shape = RoundedCornerShape(15.dp),
            colors = MaterialTheme.outlinedTextFieldColor,
            label = { Text("备注") },
            modifier = modifier,
        )
    }
}


@Composable
fun PasswordField(
    contentState: MutableState<String>,
    modifier: Modifier = Modifier,
    visualState: MutableState<Boolean> = mutableStateOf(false),
    placeholder: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
) {
    OutlinedTextField(
        value = contentState.value,
        onValueChange = { contentState.value = it },
        singleLine = true,
        shape = InputFieldDefaults.shape,
        colors = MaterialTheme.outlinedTextFieldColor,
        modifier = modifier,
        isError = isError,
        visualTransformation = if (!visualState.value) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        placeholder = placeholder,
        leadingIcon = { Icon(Icons.Rounded.Lock, contentDescription = null) },
        trailingIcon = {
            IconButton(onClick = { visualState.value = !visualState.value }) {
                Icon(
                    imageVector = if (visualState.value) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                    contentDescription = null
                )
            }
        }
    )
}


object InputFieldDefaults {
    val MinWidth = 280.dp
    val MinHeight = 55.dp
    val shape = RoundedCornerShape(50)
}


@Preview
@Composable
private fun Preview() {
    AppTheme {
        InputField(remember { mutableStateOf("") })
    }
}

