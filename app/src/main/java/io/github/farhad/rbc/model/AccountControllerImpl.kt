package io.github.farhad.rbc.model

import com.rbc.rbcaccountlibrary.Account
import io.github.farhad.rbc.data.AccountRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface AccountController {
    fun getAccounts(): Flow<List<Account>>
}

class AccountControllerImpl @Inject constructor(private val repository: AccountRepository) :
    AccountController {
    override fun getAccounts(): Flow<List<Account>> = repository.getAccounts()
}