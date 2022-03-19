package io.github.farhad.rbc.model.controller

import com.rbc.rbcaccountlibrary.Account
import io.github.farhad.rbc.data.AccountRepository
import io.github.farhad.rbc.model.Result
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

interface AccountController {
    suspend fun getAccountsAsync(): Deferred<Result<Account>>
}

class AccountControllerImpl @Inject constructor(private val repository: AccountRepository) : AccountController {
    override suspend fun getAccountsAsync(): Deferred<Result<Account>> {
        return coroutineScope {
            return@coroutineScope repository.getAccountsAsync()
        }
    }
}