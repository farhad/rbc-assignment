package io.github.farhad.rbc.di.modules

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import io.github.farhad.rbc.di.ViewModelKey
import io.github.farhad.rbc.ui.launcher.LauncherViewModel

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(LauncherViewModel::class)
    abstract fun provideLauncherViewModel(viewModel: LauncherViewModel): ViewModel
}