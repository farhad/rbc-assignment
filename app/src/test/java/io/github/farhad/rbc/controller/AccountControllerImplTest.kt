package io.github.farhad.rbc.controller

import com.rbc.rbcaccountlibrary.Account
import com.rbc.rbcaccountlibrary.AccountType
import com.rbc.rbcaccountlibrary.Transaction
import io.github.farhad.rbc.TestUtils
import io.github.farhad.rbc.data.AccountRepository
import io.github.farhad.rbc.model.AccountControllerImpl
import io.github.farhad.rbc.model.Result
import kotlinx.coroutines.*
import org.junit.Assert
import org.junit.Test

class AccountControllerImplTest {

    @Test
    fun when_repository_getAccountAsync_throws_exception_it_returns_failure_result() = runBlocking {
        // arrange
        class MockedRepository : AccountRepository {
            override suspend fun getAccountsAsync(): Deferred<Result<Account>> {
                throw IllegalStateException()
            }

            override suspend fun getTransactionsAsync(accountNumber: String, accountType: AccountType): Deferred<Result<Transaction>> {
                throw IllegalStateException()
            }
        }

        val mockedRepository = MockedRepository()
        val controller = AccountControllerImpl(mockedRepository, Dispatchers.IO)

        // act
        val result = controller.getAccountsAsync().await()

        // assert
        Assert.assertTrue(result is Result.Failure<Account>)
    }

    @Test
    fun when_repository_getAccountAsync_returns_success_it_returns_success_result() = runBlocking {
        // arrange
        val account = TestUtils.newAccount("test-account-one", "2323", "100.23", AccountType.CHEQUING)

        class MockedRepository : AccountRepository {
            override suspend fun getAccountsAsync(): Deferred<Result<Account>> {
                return coroutineScope {
                    return@coroutineScope async { Result.Success(data = listOf(account)) }
                }
            }

            override suspend fun getTransactionsAsync(accountNumber: String, accountType: AccountType): Deferred<Result<Transaction>> {
                throw IllegalStateException()
            }
        }

        val mockedRepository = MockedRepository()
        val controller = AccountControllerImpl(mockedRepository, Dispatchers.IO)

        // act
        val result = controller.getAccountsAsync().await()

        // assert
        Assert.assertTrue(result is Result.Success<Account>)
        result as Result.Success<Account>
        Assert.assertTrue(result.data.isNotEmpty())
        Assert.assertTrue(result.data.size == 1)
        Assert.assertTrue(result.data[0] == account)
    }
}