package io.github.farhad.rbc.di

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import io.github.farhad.rbc.di.modules.*
import io.github.farhad.rbc.ui.App

@AppScope
@Component(
    modules = [
        AndroidInjectionModule::class,
        ActivityModule::class,
        FragmentsModule::class,
        ControllerModule::class,
        ViewModelModule::class,
        DataProviderModule::class,
        RepositoryModule::class]
)
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: App): Builder
        fun build(): AppComponent
    }

    fun inject(app: App)
}