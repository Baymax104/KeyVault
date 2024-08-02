package top.baymaxam.keyvault.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import top.baymaxam.keyvault.ui.theme.AppTheme

/**
 * 搜索框
 * @author John
 * @since 27 6月 2024
 */

@Composable
fun SearchField(
    content: MutableState<String> = mutableStateOf(""),
    placeholder: @Composable () -> Unit,
    onSearch: () -> Unit = {}
) {
    OutlinedTextField(
        value = content.value,
        onValueChange = { content.value = it },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(50),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            unfocusedLeadingIconColor = Color.Black,
            focusedLeadingIconColor = MaterialTheme.colorScheme.primary
        ),
        singleLine = true,
        placeholder = placeholder,
        leadingIcon = {
            IconButton(onClick = onSearch) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = null,
                )
            }
        },
        trailingIcon = {
            if (content.value.isNotEmpty()) {
                IconButton(onClick = { content.value = "" }) {
                    Icon(imageVector = Icons.Rounded.Close, contentDescription = null)
                }
            }
        }
    )
}

@Composable
fun InfoField(
    content: MutableState<String> = mutableStateOf(""),
    placeholder: @Composable () -> Unit = {},
    leadingIcon: @Composable () -> Unit = {},
) {
    OutlinedTextField(
        value = content.value,
        onValueChange = { content.value = it },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(50),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            unfocusedLeadingIconColor = Color.Black,
            focusedLeadingIconColor = MaterialTheme.colorScheme.primary
        ),
        singleLine = true,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = {
            if (content.value.isNotEmpty()) {
                IconButton(onClick = { content.value = "" }) {
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
        InfoField()
    }
}

