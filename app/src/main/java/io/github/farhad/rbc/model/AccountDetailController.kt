package io.github.farhad.rbc.model

import com.rbc.rbcaccountlibrary.AccountType
import com.rbc.rbcaccountlibrary.Transaction
import io.github.farhad.rbc.data.AccountRepository
import io.github.farhad.rbc.di.modules.IoDispatcher
import io.github.farhad.rbc.ui.util.isNotNullOrEmpty
import kotlinx.coroutines.*
import javax.inject.Inject

interface AccountDetailController {
    suspend fun getTransactionsAsync(accountNumber: String, accountType: AccountType): Deferred<Result<Transaction>>
}

class AccountDetailControllerImpl @Inject constructor(
    private val repository: AccountRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : AccountDetailController {

    override suspend fun getTransactionsAsync(accountNumber: String, accountType: AccountType): Deferred<Result<Transaction>> {
        return coroutineScope {
            try {
                if (validateAccountNumber(accountNumber)) {
                    withContext(ioDispatcher) {
                        return@withContext repository.getTransactionsAsync(accountNumber, accountType)
                    }
                } else {
                    return@coroutineScope async { return@async Result.InputError<Transaction>() }
                }
            } catch (e: Exception) {
                return@coroutineScope async { Result.Failure<Transaction>() }
            }
        }
    }

    private fun validateAccountNumber(number: String?): Boolean = number.isNotNullOrEmpty()
}