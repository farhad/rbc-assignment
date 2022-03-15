package io.github.farhad.rbc.di.modules

import dagger.Module
import dagger.Provides
import io.github.farhad.rbc.data.AccountRepository
import io.github.farhad.rbc.model.AccountController
import io.github.farhad.rbc.model.AccountControllerImpl
import io.github.farhad.rbc.model.AccountDetailController
import io.github.farhad.rbc.model.AccountDetailControllerImpl
import kotlinx.coroutines.CoroutineDispatcher

@Module
class ControllerModule {

    @Provides
    fun provideAccountController(repository: AccountRepository, @IoDispatcher ioDispatcher: CoroutineDispatcher): AccountController =
        AccountControllerImpl(repository, ioDispatcher)

    @Provides
    fun provideAccountDetailController(
        repository: AccountRepository,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): AccountDetailController = AccountDetailControllerImpl(repository, ioDispatcher)
}