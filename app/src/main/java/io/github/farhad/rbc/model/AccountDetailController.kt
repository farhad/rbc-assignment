package io.github.farhad.rbc.model

import com.rbc.rbcaccountlibrary.AccountType
import com.rbc.rbcaccountlibrary.Transaction
import io.github.farhad.rbc.data.AccountRepository
import io.github.farhad.rbc.ui.util.isNotNullOrEmpty
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

interface AccountDetailController {
    suspend fun getTransactionsAsync(accountNumber: String, accountType: AccountType): Deferred<Result<Transaction>>
}

class AccountDetailControllerImpl @Inject constructor(private val repository: AccountRepository) : AccountDetailController {

    override suspend fun getTransactionsAsync(accountNumber: String, accountType: AccountType): Deferred<Result<Transaction>> {
        return coroutineScope {
            if (validateAccountNumber(accountNumber)) {
                return@coroutineScope repository.getTransactionsAsync(accountNumber, accountType)
            } else {
                return@coroutineScope async { return@async Result.InputError<Transaction>() }
            }
        }
    }

    private fun validateAccountNumber(number: String?): Boolean = number.isNotNullOrEmpty()
}