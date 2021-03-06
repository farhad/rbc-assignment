package io.github.farhad.rbc.di.modules

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import io.github.farhad.rbc.di.ViewModelKey
import io.github.farhad.rbc.ui.account.detail.AccountDetailsViewModel
import io.github.farhad.rbc.ui.account.list.AccountsViewModel
import io.github.farhad.rbc.ui.splash.SplashViewModel

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun provideSplashViewModel(viewModel: SplashViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AccountsViewModel::class)
    abstract fun provideAccountViewModel(viewModel: AccountsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AccountDetailsViewModel::class)
    abstract fun provideAccountDetailViewModel(viewModel: AccountDetailsViewModel): ViewModel
}