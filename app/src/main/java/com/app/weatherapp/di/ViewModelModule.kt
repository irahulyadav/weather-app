package com.app.weatherapp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.weatherapp.viewmodel.WeatherInfoViewModel
import com.appszum.idl.di.ViewModelFactory
import com.appszum.idl.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
//
//    @Binds
//    @IntoMap
//    @ViewModelKey(BreedViewModel::class)
//    protected abstract fun breedViewModel(breedViewModel: BreedViewModel): ViewModel
//
//
//    @Binds
//    @IntoMap
//    @ViewModelKey(ImageListViewModel::class)
//    protected abstract fun imageListViewModel(imageListViewModel: ImageListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WeatherInfoViewModel::class)
    protected abstract fun weatherInfoViewModel(weatherInfoViewModel: WeatherInfoViewModel): ViewModel

}