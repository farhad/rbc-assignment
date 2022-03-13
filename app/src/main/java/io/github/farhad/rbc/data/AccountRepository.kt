package io.github.farhad.rbc.data

import com.rbc.rbcaccountlibrary.Account
import com.rbc.rbcaccountlibrary.Transaction
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class AccountRepository @Inject constructor(private val provider: AccountDataProvider) {

    suspend fun getAccountsAsync(): Deferred<List<Account>> {
        return coroutineScope { return@coroutineScope async { provider.getAccounts() } }
    }

    suspend fun getTransactionsAsync(accountNumber: String): Deferred<List<Transaction>> {
        return coroutineScope { return@coroutineScope async { provider.getAccountTransactions(accountNumber) } }
    }

    suspend fun getAdditionalCreditCardTransactionsAsync(accountNumber: String): Deferred<List<Transaction>> {
        return coroutineScope { return@coroutineScope async { provider.getAdditionalCreditCardTransactions(accountNumber) } }
    }
}