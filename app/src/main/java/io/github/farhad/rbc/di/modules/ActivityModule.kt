package io.github.farhad.rbc.di.modules

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.farhad.rbc.ui.home.HomeActivity

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun provideLauncherActivity(): HomeActivity
}