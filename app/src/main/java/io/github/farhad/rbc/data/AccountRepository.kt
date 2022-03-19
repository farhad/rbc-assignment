package io.github.farhad.rbc.data

import com.rbc.rbcaccountlibrary.Account
import com.rbc.rbcaccountlibrary.AccountType
import com.rbc.rbcaccountlibrary.Transaction
import io.github.farhad.rbc.di.modules.IoDispatcher
import io.github.farhad.rbc.model.Result
import kotlinx.coroutines.*
import javax.inject.Inject

interface AccountRepository {
    suspend fun getAccountsAsync(): Deferred<Result<Account>>
    suspend fun getTransactionsAsync(accountNumber: String, accountType: AccountType): Deferred<Result<Transaction>>
}

class AccountRepositoryImpl @Inject constructor(
    private val provider: AccountDataProvider,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : AccountRepository {

    override suspend fun getAccountsAsync(): Deferred<Result<Account>> {
        return coroutineScope {
            try {
                withContext(ioDispatcher) {
                    return@withContext async {
                        delay(1500)
                        Result.Success(data = provider.getAccounts())
                    }
                }
            } catch (e: Exception) {
                return@coroutineScope async { Result.Failure<Account>() }
            }
        }
    }

    override suspend fun getTransactionsAsync(accountNumber: String, accountType: AccountType): Deferred<Result<Transaction>> {
        return coroutineScope {
            try {
                withContext(ioDispatcher) {
                    val accountJob = async {
                        return@async try {
                            provider.getAccountTransactions(accountNumber)
                        } catch (e: Exception) {
                            return@async emptyList()
                        }
                    }

                    val creditCardJob = async {
                        return@async try {
                            if (accountType == AccountType.CREDIT_CARD)
                                provider.getAdditionalCreditCardTransactions(accountNumber)
                            else emptyList()
                        } catch (e: Exception) {
                            return@async emptyList()
                        }
                    }

                    return@withContext async {
                        val (accountTransactions, creditCardTransactions) = awaitAll(accountJob, creditCardJob)
                        Result.Success(accountTransactions.plus(creditCardTransactions).sortedByDescending { it.date.timeInMillis })
                    }
                }

            } catch (e: Exception) {
                return@coroutineScope async { Result.Failure<Transaction>() }
            }
        }
    }
}