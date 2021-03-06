package io.github.farhad.rbc

import com.rbc.rbcaccountlibrary.Account
import com.rbc.rbcaccountlibrary.AccountType
import com.rbc.rbcaccountlibrary.Transaction
import io.github.farhad.rbc.ui.account.list.AccountDataItem
import io.github.farhad.rbc.ui.util.getFriendlyTitle
import java.util.*

object TestUtils {
    /**
     * using reflection to instantiate Account objects
     */
    fun newAccount(name: String, number: String, balance: String, type: AccountType): Account {
        val constructor = Account::class.java.declaredConstructors[0]
        constructor.isAccessible = true
        return constructor.newInstance(name, number, balance, type) as Account
    }

    fun newTransaction(description: String, amount: String, date: Calendar): Transaction {
        val constructor = Transaction::class.java.declaredConstructors[0]
        constructor.isAccessible = true
        return constructor.newInstance(description, amount, date) as Transaction
    }

    fun Account.toAccountDataItem(): AccountDataItem.Item {
        return AccountDataItem.Item(name, number, balance, Currency.getInstance(Locale.CANADA).symbol, type.getFriendlyTitle())
    }
}