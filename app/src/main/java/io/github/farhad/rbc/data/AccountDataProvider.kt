package io.github.farhad.rbc.data

import com.rbc.rbcaccountlibrary.Account
import com.rbc.rbcaccountlibrary.AccountProvider
import com.rbc.rbcaccountlibrary.Transaction

interface AccountDataProvider {
    fun getAccounts(): List<Account>
    fun getAccountTransactions(accountNumber: String): List<Transaction>
    fun getAdditionalCreditCardTransactions(accountNumber: String): List<Transaction>
}

/**
 * todo : create a cache here to persist data during user's session and get rid of it when user exits
 */
class AccountDataProviderImpl(private val provider: AccountProvider) : AccountDataProvider {
    override fun getAccounts(): List<Account> = provider.getAccountsList()

    override fun getAccountTransactions(accountNumber: String): List<Transaction> {
        return try {
            provider.getTransactions(accountNumber)
        } catch (e: Exception) {
            listOf()
        }
    }

    override fun getAdditionalCreditCardTransactions(accountNumber: String): List<Transaction> =
        provider.getAdditionalCreditCardTransactions(accountNumber)
}