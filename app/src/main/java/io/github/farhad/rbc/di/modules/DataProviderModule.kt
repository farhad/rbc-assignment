package io.github.farhad.rbc.di.modules

import com.rbc.rbcaccountlibrary.AccountProvider
import dagger.Module
import dagger.Provides
import io.github.farhad.rbc.data.AccountDataProvider
import io.github.farhad.rbc.data.AccountDataProviderImpl

@Module
class DataProviderModule {

    @Provides
    fun provideAccountDataProvider(): AccountDataProvider = AccountDataProviderImpl(AccountProvider)
}