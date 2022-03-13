package io.github.farhad.rbc.model

import com.rbc.rbcaccountlibrary.Account
import io.github.farhad.rbc.data.AccountRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.delay
import javax.inject.Inject

interface AccountController {
    suspend fun getAccountsAsync(): Deferred<List<Account>>
}

class AccountControllerImpl @Inject constructor(private val repository: AccountRepository) : AccountController {
    override suspend fun getAccountsAsync(): Deferred<List<Account>> {
        delay(1500) // to emulate blocking work
        return repository.getAccountsAsync()
    }
}