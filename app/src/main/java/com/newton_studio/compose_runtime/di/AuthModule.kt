package com.newton_studio.compose_runtime.di

import com.newton_studio.compose_runtime.data.IAuthRepository
import com.newton_studio.compose_runtime.domain.AuthRepository
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
class AuthModule {
    @Provides
    @Singleton
    fun provideRepository(): IAuthRepository = AuthRepository()

    @Provides
    @Singleton
    fun provideScope() = CoroutineScope(SupervisorJob() + Dispatchers.IO)
}