package io.github.farhad.rbc.di.modules

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.farhad.rbc.ui.account.AccountsFragment
import io.github.farhad.rbc.ui.account.detail.AccountDetailFragment
import io.github.farhad.rbc.ui.splash.SplashFragment

@Module
abstract class FragmentsModule {

    @ContributesAndroidInjector
    abstract fun provideSplashFragment(): SplashFragment

    @ContributesAndroidInjector
    abstract fun provideAccountFragment(): AccountsFragment

    @ContributesAndroidInjector
    abstract fun provideAccountDetailFragment(): AccountDetailFragment
}