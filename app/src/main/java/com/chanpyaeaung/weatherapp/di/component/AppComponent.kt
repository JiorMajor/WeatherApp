package com.chanpyaeaung.weatherapp.di.component

import android.app.Application
import com.chanpyaeaung.weatherapp.WeatherApp
import com.chanpyaeaung.weatherapp.di.modules.ActivityModule
import com.chanpyaeaung.weatherapp.di.modules.AppModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ActivityModule::class, AppModule::class, AndroidSupportInjectionModule::class))
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }

    fun inject(app: WeatherApp)

}