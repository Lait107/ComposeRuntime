package com.newton_studio.compose_runtime.domain.model

sealed interface AuthEvent {
    data class Login(
        val login: String,
        val password: String,
    ) : AuthEvent
    data object Logout: AuthEvent
}