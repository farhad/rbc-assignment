package io.github.farhad.rbc.data

import com.rbc.rbcaccountlibrary.Account
import com.rbc.rbcaccountlibrary.AccountType
import com.rbc.rbcaccountlibrary.Transaction
import io.github.farhad.rbc.model.Result
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import javax.inject.Inject

class AccountRepository @Inject constructor(private val provider: AccountDataProvider) {

    suspend fun getAccountsAsync(): Deferred<Result<Account>> {
        return coroutineScope {
            return@coroutineScope async {
                try {
                    delay(1500) // to emulate blocking work
                    return@async Result.Success(data = provider.getAccounts())
                } catch (e: Exception) {
                    return@async Result.Failure<Account>()
                }
            }
        }
    }

    suspend fun getTransactionsAsync(accountNumber: String, accountType: AccountType): Deferred<Result<Transaction>> {
        return coroutineScope {
            return@coroutineScope async {

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

                return@async Result.Success((accountJob.await() + creditCardJob.await()).sortedByDescending { it.date.timeInMillis })
            }
        }
    }
}