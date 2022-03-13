package io.github.farhad.rbc.di.modules

import dagger.Module
import dagger.Provides
import io.github.farhad.rbc.data.AccountDataProvider
import io.github.farhad.rbc.data.AccountRepository
import io.github.farhad.rbc.di.AppScope

@Module
class RepositoryModule {

    @AppScope
    @Provides
    fun provideAccountsRepository(accountDataProvider: AccountDataProvider) =
        AccountRepository(accountDataProvider)
}