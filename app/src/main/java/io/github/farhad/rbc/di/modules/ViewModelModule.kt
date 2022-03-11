package io.github.farhad.rbc.di.modules

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import io.github.farhad.rbc.di.ViewModelKey
import io.github.farhad.rbc.ui.home.HomeViewModel
import io.github.farhad.rbc.ui.splash.SplashViewModel

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun provideHomeViewModel(viewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun provideSplashViewModel(viewModel: SplashViewModel): ViewModel
}