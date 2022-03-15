package io.github.farhad.rbc.model

import com.rbc.rbcaccountlibrary.Account
import io.github.farhad.rbc.data.AccountRepository
import io.github.farhad.rbc.di.modules.IoDispatcher
import kotlinx.coroutines.*
import javax.inject.Inject

interface AccountController {
    suspend fun getAccountsAsync(): Deferred<Result<Account>>
}

class AccountControllerImpl @Inject constructor(
    private val repository: AccountRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : AccountController {
    override suspend fun getAccountsAsync(): Deferred<Result<Account>> {
        return coroutineScope {
            try {
                withContext(ioDispatcher) {
                    return@withContext repository.getAccountsAsync()
                }

            } catch (e: Exception) {
                return@coroutineScope async { Result.Failure<Account>() }
            }
        }

    }
}