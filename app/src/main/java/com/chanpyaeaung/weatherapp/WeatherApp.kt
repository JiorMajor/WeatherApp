package com.chanpyaeaung.weatherapp

import android.app.Activity
import android.app.Application
import android.content.Context
import com.chanpyaeaung.weatherapp.di.component.DaggerAppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class WeatherApp: Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    lateinit var context: Context

    override fun onCreate() {
        super.onCreate()
        this.initDagger()
        context = applicationContext
    }

    override fun activityInjector(): DispatchingAndroidInjector<Activity>? {
        return dispatchingAndroidInjector
    }

    // ---

    private fun initDagger() {
        DaggerAppComponent.builder().application(this).build().inject(this)
    }


}