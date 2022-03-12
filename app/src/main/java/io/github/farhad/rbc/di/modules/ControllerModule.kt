package io.github.farhad.rbc.di.modules

import dagger.Module
import dagger.Provides
import io.github.farhad.rbc.data.AccountsRepository
import io.github.farhad.rbc.model.AccountsController

@Module
class ControllerModule {

    @Provides
    fun provideAccountsController(repository: AccountsRepository) = AccountsController(repository)
}