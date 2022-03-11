package io.github.farhad.rbc.data

import com.rbc.rbcaccountlibrary.Account
import com.rbc.rbcaccountlibrary.AccountProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AccountsRepository @Inject constructor(private val provider: AccountProvider) {

    fun getAccounts(): Flow<List<Account>> {
        return flow { emit(provider.getAccountsList()) }
    }
}