package io.github.farhad.rbc.model

import com.rbc.rbcaccountlibrary.AccountType
import com.rbc.rbcaccountlibrary.Transaction
import io.github.farhad.rbc.data.AccountRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

interface AccountDetailController {
    suspend fun getTransactionsAsync(accountNumber: String, accountType: AccountType): Deferred<List<Transaction>>
}

class AccountDetailControllerImpl @Inject constructor(private val repository: AccountRepository) : AccountDetailController {

    override suspend fun getTransactionsAsync(accountNumber: String, accountType: AccountType): Deferred<List<Transaction>> {
        return coroutineScope {
            val transactions = repository.getTransactionsAsync(accountNumber)
            val additionalTransactions = if (accountType == AccountType.CREDIT_CARD)
                repository.getAdditionalCreditCardTransactionsAsync(accountNumber)
            else async { listOf() }

            return@coroutineScope async { (transactions.await() + additionalTransactions.await()).sortedByDescending { it.date.timeInMillis } }
        }
    }
}