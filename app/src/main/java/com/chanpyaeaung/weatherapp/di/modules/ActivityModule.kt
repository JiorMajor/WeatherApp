package com.chanpyaeaung.weatherapp.di.modules

import com.chanpyaeaung.weatherapp.views.activities.AddCityActivity
import dagger.Module
import com.chanpyaeaung.weatherapp.views.activities.MainActivity
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributeAddCityActivity(): AddCityActivity
}