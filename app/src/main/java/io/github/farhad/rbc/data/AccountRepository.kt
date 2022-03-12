package io.github.farhad.rbc.data

import com.rbc.rbcaccountlibrary.Account
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AccountRepository @Inject constructor(private val provider: AccountDataProvider) {

    fun getAccounts(): Flow<List<Account>> {
        return flow { emit(provider.getAccounts()) }
    }
}