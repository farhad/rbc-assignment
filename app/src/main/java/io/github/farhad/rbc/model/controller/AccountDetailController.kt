package io.github.farhad.rbc.model.controller

import com.rbc.rbcaccountlibrary.AccountType
import com.rbc.rbcaccountlibrary.Transaction
import io.github.farhad.rbc.data.AccountRepository
import io.github.farhad.rbc.model.Result
import io.github.farhad.rbc.model.validator.AccountDetailInputValidator
import io.github.farhad.rbc.model.validator.AccountValidationResult
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

interface AccountDetailController {
    suspend fun getTransactionsAsync(accountNumber: String, accountType: AccountType): Deferred<Result<Transaction>>
}

class AccountDetailControllerImpl @Inject constructor(
    private val repository: AccountRepository,
    private val validator: AccountDetailInputValidator
) : AccountDetailController {

    override suspend fun getTransactionsAsync(accountNumber: String, accountType: AccountType): Deferred<Result<Transaction>> {
        return coroutineScope {
            try {
                when (validator.validate(accountNumber)) {
                    AccountValidationResult.SUCCESS -> return@coroutineScope repository.getTransactionsAsync(accountNumber, accountType)
                    AccountValidationResult.ACCOUNT_NUMBER_EMPTY -> return@coroutineScope async { return@async Result.InputError<Transaction>() }
                }
            } catch (e: Exception) {
                return@coroutineScope async { return@async Result.Failure<Transaction>() }
            }
        }
    }
}