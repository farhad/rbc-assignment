package io.github.farhad.rbc.di.modules

import dagger.Module
import dagger.Provides
import io.github.farhad.rbc.data.AccountDataProvider
import io.github.farhad.rbc.data.AccountRepository

@Module
class RepositoryModule {

    @Provides
    fun provideAccountsRepository(accountDataProvider: AccountDataProvider) =
        AccountRepository(accountDataProvider)
}