package io.github.farhad.rbc.di.modules

import com.rbc.rbcaccountlibrary.AccountProvider
import dagger.Module
import dagger.Provides
import io.github.farhad.rbc.data.AccountsRepository

@Module
class RepositoryModule {

    @Provides
    fun provideAccountsRepository(accountProvider: AccountProvider) =
        AccountsRepository(accountProvider)
}