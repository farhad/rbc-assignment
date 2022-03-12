package io.github.farhad.rbc.model

import com.rbc.rbcaccountlibrary.Account
import io.github.farhad.rbc.data.AccountsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AccountsController @Inject constructor(private val repository: AccountsRepository) {
    fun getAccounts(): Flow<List<Account>> = repository.getAccounts()
}