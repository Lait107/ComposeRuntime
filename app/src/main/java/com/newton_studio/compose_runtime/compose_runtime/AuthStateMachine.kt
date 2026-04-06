package com.newton_studio.compose_runtime.compose_runtime

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.newton_studio.compose_runtime.data.IAuthRepository
import com.newton_studio.compose_runtime.data.toDomainUser
import com.newton_studio.compose_runtime.domain.model.AuthEvent
import com.newton_studio.compose_runtime.domain.model.AuthState

@Composable
fun AuthStateMachine(
    event: AuthEvent,
    repo: IAuthRepository,
    onState: (AuthState) -> Unit
) {
    var state by remember { mutableStateOf<AuthState>(AuthState.LoggedOut) }

    fun loading() {
        state = AuthState.Loading
        onState(state)
    }

    LaunchedEffect(event) {
        loading()
        when (event) {
            is AuthEvent.Login -> {
                val user = repo.login(event.login, event.password).toDomainUser()
                state = AuthState.LoggedIn(user)
            }
            AuthEvent.Logout -> {
                repo.logout()
                state = AuthState.LoggedOut
            }
        }
        onState(state)
    }
}