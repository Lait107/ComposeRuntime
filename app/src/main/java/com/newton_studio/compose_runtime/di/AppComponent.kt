package com.newton_studio.compose_runtime.di

import com.newton_studio.compose_runtime.LoginScreen
import com.newton_studio.compose_runtime.domain.AuthInteractor
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AuthModule::class])
interface AppComponent {
    fun getAuthInteractor(): AuthInteractor

     fun inject(activity: LoginScreen)
}