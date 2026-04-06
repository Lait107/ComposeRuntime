package com.newton_studio.compose_runtime.data


interface IAuthRepository {
    suspend fun login(
        login: String,
        password: String,
    ) : User

    suspend fun logout()
}