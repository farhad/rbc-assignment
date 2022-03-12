package io.github.farhad.rbc.data

import com.rbc.rbcaccountlibrary.Account
import com.rbc.rbcaccountlibrary.AccountProvider

interface AccountDataProvider {
    fun getAccounts(): List<Account>
}

class AccountDataProviderImpl(private val provider: AccountProvider) : AccountDataProvider {
    override fun getAccounts(): List<Account> = provider.getAccountsList()
}