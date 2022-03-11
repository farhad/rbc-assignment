package io.github.farhad.rbc.di.modules

import com.rbc.rbcaccountlibrary.AccountProvider
import dagger.Module
import dagger.Provides

@Module
class AccountProviderModule {

    @Provides
    fun provideAccountProvider(): AccountProvider = AccountProvider
}