package io.github.farhad.rbc.di.modules

import dagger.Module
import dagger.Provides
import io.github.farhad.rbc.data.AccountRepository
import io.github.farhad.rbc.model.controller.AccountController
import io.github.farhad.rbc.model.controller.AccountControllerImpl
import io.github.farhad.rbc.model.controller.AccountDetailController
import io.github.farhad.rbc.model.controller.AccountDetailControllerImpl
import io.github.farhad.rbc.model.validator.AccountDetailInputValidator

@Module
class ControllerModule {

    @Provides
    fun provideAccountController(repository: AccountRepository): AccountController = AccountControllerImpl(repository)

    @Provides
    fun provideAccountDetailController(repository: AccountRepository, validator: AccountDetailInputValidator): AccountDetailController =
        AccountDetailControllerImpl(repository, validator)
}