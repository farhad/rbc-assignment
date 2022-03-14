package io.github.farhad.rbc.data

import com.rbc.rbcaccountlibrary.Account
import com.rbc.rbcaccountlibrary.AccountProvider
import com.rbc.rbcaccountlibrary.Transaction

interface AccountDataProvider {
    suspend fun getAccounts(): List<Account>
    suspend fun getAccountTransactions(accountNumber: String): List<Transaction>
    suspend fun getAdditionalCreditCardTransactions(accountNumber: String): List<Transaction>
}

class AccountDataProviderImpl(private val provider: AccountProvider) : AccountDataProvider {
    override suspend fun getAccounts(): List<Account> = provider.getAccountsList()

    override suspend fun getAccountTransactions(accountNumber: String): List<Transaction> = provider.getTransactions(accountNumber)

    override suspend fun getAdditionalCreditCardTransactions(accountNumber: String): List<Transaction> =
        provider.getAdditionalCreditCardTransactions(accountNumber)
}