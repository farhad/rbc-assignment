package io.github.farhad.rbc.di.modules

import dagger.Module
import dagger.Provides
import io.github.farhad.rbc.data.AccountsRepository

@Module
class RepositoryModule {

    @Provides
    fun provideAccountsRepository() = AccountsRepository()
}