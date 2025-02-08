package top.baymaxam.keyvault.state

import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.MutableStateFlow
import top.baymaxam.keyvault.model.domain.VerifyState

/**
 * AuthState
 * @author John
 * @since 09 2月 2025
 */
@Stable
class AuthState {
    private val state: MutableStateFlow<VerifyState> = MutableStateFlow(VerifyState.Default)

    var value: VerifyState
        get() = state.value
        set(value) {
            state.value = value
        }
}