package io.github.farhad.rbc.di.modules

import dagger.Module
import dagger.Provides
import io.github.farhad.rbc.data.AccountRepository
import io.github.farhad.rbc.model.AccountController
import io.github.farhad.rbc.model.AccountControllerImpl

@Module
class ControllerModule {

    @Provides
    fun provideAccountController(repository: AccountRepository): AccountController =
        AccountControllerImpl(repository)
}