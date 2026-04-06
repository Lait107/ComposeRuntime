package com.newton_studio.compose_runtime.domain

import com.newton_studio.compose_runtime.data.IAuthRepository
import com.newton_studio.compose_runtime.data.User
import kotlinx.coroutines.delay
import javax.inject.Inject

class AuthRepository @Inject constructor() : IAuthRepository {
    override suspend fun login(
        login: String,
        password: String
    ): User {
        delay(1_500L)
        return User(login, password)
    }

    override suspend fun logout() {
        delay(1_000L)
    }
}