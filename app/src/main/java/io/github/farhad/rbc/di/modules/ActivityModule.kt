package io.github.farhad.rbc.di.modules

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.farhad.rbc.ui.launcher.LauncherActivity

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun provideLauncherActivity(): LauncherActivity
}