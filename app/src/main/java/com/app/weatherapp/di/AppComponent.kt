package com.app.weatherapp.di

import com.app.weatherapp.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, RetrofitModule::class, ViewModelModule::class])
interface AppComponent {
    fun inject(application: MainActivity)
}