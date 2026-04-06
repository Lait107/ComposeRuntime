package com.newton_studio.compose_runtime

import android.app.Application
import com.newton_studio.compose_runtime.di.AppComponent
import com.newton_studio.compose_runtime.di.AuthModule
import com.newton_studio.compose_runtime.di.DaggerAppComponent
import kotlin.properties.Delegates

class App : Application() {
    var appComponent: AppComponent by Delegates.notNull<AppComponent>()

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .authModule(AuthModule())
            .build()
    }
}