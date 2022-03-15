package io.github.farhad.rbc.controller

import com.rbc.rbcaccountlibrary.Account
import com.rbc.rbcaccountlibrary.AccountType
import com.rbc.rbcaccountlibrary.Transaction
import io.github.farhad.rbc.TestUtils
import io.github.farhad.rbc.data.AccountRepository
import io.github.farhad.rbc.model.AccountDetailControllerImpl
import io.github.farhad.rbc.model.Result
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import java.util.*

class AccountDetailControllerImplTest {

    @Test
    fun when_repository_getTransactionsAsync_throws_exception_it_returns_failure_result() = runBlocking {
        // arrange
        val account = TestUtils.newAccount("test-account-one", "2323", "100.23", AccountType.CHEQUING)

        class MockedRepository : AccountRepository {
            override suspend fun getAccountsAsync(): Deferred<Result<Account>> {
                throw IllegalStateException()
            }

            override suspend fun getTransactionsAsync(accountNumber: String, accountType: AccountType): Deferred<Result<Transaction>> {
                throw IllegalStateException()
            }
        }

        val mockedRepository = MockedRepository()
        val controller = AccountDetailControllerImpl(mockedRepository)

        // act
        val result = controller.getTransactionsAsync(account.number, account.type).await()

        // assert
        Assert.assertTrue(result is Result.Failure<Transaction>)
    }

    @Test
    fun when_account_number_is_empty_or_null_it_returns_input_error_result() = runBlocking {
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
        val controller = AccountDetailControllerImpl(mockedRepository)

        // act
        var result = controller.getTransactionsAsync("", AccountType.MORTGAGE).await()

        // assert
        Assert.assertTrue(result is Result.InputError<Transaction>)

        // act
        result = controller.getTransactionsAsync("null", AccountType.MORTGAGE).await()

        // assert
        Assert.assertTrue(result is Result.InputError<Transaction>)
    }

    @Test
    fun when_repository_getTransactionsAsync_returns_success_result_it_returns_success_result() = runBlocking {
        // arrange
        val transaction = TestUtils.newTransaction("test-transaction", "23.56", Calendar.getInstance())

        class MockedRepository : AccountRepository {
            override suspend fun getAccountsAsync(): Deferred<Result<Account>> {
                throw IllegalStateException()
            }

            override suspend fun getTransactionsAsync(accountNumber: String, accountType: AccountType): Deferred<Result<Transaction>> {
                return coroutineScope {
                    return@coroutineScope async { Result.Success(listOf(transaction)) }
                }
            }
        }

        val mockedRepository = MockedRepository()
        val controller = AccountDetailControllerImpl(mockedRepository)

        // act
        val result = controller.getTransactionsAsync("1225", AccountType.CREDIT_CARD).await()

        // assert
        Assert.assertTrue(result is Result.Success<Transaction>)
        result as Result.Success<Transaction>
        Assert.assertTrue(result.data.isNotEmpty())
        Assert.assertTrue(result.data.size == 1)
        Assert.assertTrue(result.data[0] == transaction)
    }
}