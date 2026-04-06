package com.newton_studio.compose_runtime.domain

import androidx.compose.runtime.Composition
import androidx.compose.runtime.Recomposer
import com.newton_studio.compose_runtime.NoOpApplier
import com.newton_studio.compose_runtime.compose_runtime.AuthStateMachine
import com.newton_studio.compose_runtime.data.IAuthRepository
import com.newton_studio.compose_runtime.domain.model.AuthEvent
import com.newton_studio.compose_runtime.domain.model.AuthState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class AuthInteractor @Inject constructor(
    private val repo: IAuthRepository,
    private val scope: CoroutineScope
) {
    private val composition = Composition(NoOpApplier(), Recomposer(scope.coroutineContext))

    private val _state = MutableStateFlow<AuthState>(AuthState.LoggedOut)
    val state: StateFlow<AuthState> = _state

    fun send(event: AuthEvent) {
        composition.setContent {
            AuthStateMachine(event, repo) { newState ->
                _state.value = newState
            }
        }
    }
}