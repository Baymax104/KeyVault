package top.baymaxam.keyvault.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color

/**
 * 搜索框
 * @author John
 * @since 27 6月 2024
 */

@Composable
fun SearchField(
    content: MutableState<String> = mutableStateOf(""),
    placeholder: String = "",
    onSearch: () -> Unit = {}
) {
    OutlinedTextField(
        value = content.value,
        onValueChange = { content.value = it },
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(50))
            .background(Color.White),
        shape = RoundedCornerShape(50),
        leadingIcon = {
            IconButton(onClick = onSearch) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            }
        },
        trailingIcon = {
            if (content.value.isNotEmpty()) {
                IconButton(onClick = { content.value = "" }) {
                    Icon(imageVector = Icons.Rounded.Close, contentDescription = null)
                }
            }
        },
        singleLine = true,
        placeholder = {
            Text(text = placeholder)
        },
    )
}
