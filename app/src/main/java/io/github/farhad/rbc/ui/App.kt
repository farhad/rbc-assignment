package io.github.farhad.rbc.ui

import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import io.github.farhad.rbc.di.AppComponent
import io.github.farhad.rbc.di.DaggerAppComponent
import javax.inject.Inject

class App : Application(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    lateinit var appComponent: AppComponent
        private set

    private fun createAppComponent(): AppComponent = DaggerAppComponent.builder().application(this).build()

    override fun onCreate() {
        super.onCreate()

        appComponent = createAppComponent()
        appComponent.inject(this)
    }
}