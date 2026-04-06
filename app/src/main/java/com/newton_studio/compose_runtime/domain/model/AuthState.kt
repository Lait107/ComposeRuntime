package com.newton_studio.compose_runtime.domain.model

sealed interface AuthState {
    data object Loading: AuthState
    data class LoggedIn(
        val user: User,
    ): AuthState
    data object LoggedOut: AuthState
}