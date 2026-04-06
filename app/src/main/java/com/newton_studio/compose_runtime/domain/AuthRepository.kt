package com.newton_studio.compose_runtime.domain

import com.newton_studio.compose_runtime.data.IAuthRepository
import com.newton_studio.compose_runtime.data.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepository @Inject constructor() : IAuthRepository {
    override suspend fun login(
        login: String,
        password: String
    ): User = withContext(Dispatchers.IO) {
        delay(1_500L)
        User(login, password)
    }

    override suspend fun logout() = withContext(Dispatchers.IO) {
        delay(1_000L)
    }
}