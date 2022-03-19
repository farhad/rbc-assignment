package io.github.farhad.rbc.di.modules

import dagger.Module
import dagger.Provides
import io.github.farhad.rbc.data.AccountRepository
import io.github.farhad.rbc.model.AccountController
import io.github.farhad.rbc.model.AccountControllerImpl
import io.github.farhad.rbc.model.AccountDetailController
import io.github.farhad.rbc.model.AccountDetailControllerImpl

@Module
class ControllerModule {

    @Provides
    fun provideAccountController(repository: AccountRepository): AccountController = AccountControllerImpl(repository)

    @Provides
    fun provideAccountDetailController(repository: AccountRepository): AccountDetailController = AccountDetailControllerImpl(repository)
}