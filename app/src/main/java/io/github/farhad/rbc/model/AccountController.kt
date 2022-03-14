package io.github.farhad.rbc.model

import com.rbc.rbcaccountlibrary.Account
import io.github.farhad.rbc.data.AccountRepository
import kotlinx.coroutines.Deferred
import javax.inject.Inject

interface AccountController {
    suspend fun getAccountsAsync(): Deferred<Result<Account>>
}

class AccountControllerImpl @Inject constructor(private val repository: AccountRepository) : AccountController {
    override suspend fun getAccountsAsync(): Deferred<Result<Account>> {
        return repository.getAccountsAsync()
    }
}