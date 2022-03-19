package io.github.farhad.rbc.repository

import com.rbc.rbcaccountlibrary.Account
import com.rbc.rbcaccountlibrary.AccountType
import com.rbc.rbcaccountlibrary.Transaction
import io.github.farhad.rbc.TestUtils.newAccount
import io.github.farhad.rbc.TestUtils.newTransaction
import io.github.farhad.rbc.data.AccountDataProvider
import io.github.farhad.rbc.data.AccountRepositoryImpl
import io.github.farhad.rbc.model.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.*

class AccountRepositoryTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    val testDispatcher = TestCoroutineDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun before() {
        Dispatchers.setMain(testDispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun after() {
        testDispatcher.cancelChildren()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun when_provider_getAccountAsync_throws_exception_it_returns_failure_result() = runBlocking {
        // arrange
        class MockedAccountDataProvider : AccountDataProvider {
            override suspend fun getAccounts(): List<Account> {
                throw IllegalStateException()
            }

            override suspend fun getAccountTransactions(accountNumber: String): List<Transaction> {
                return listOf()
            }

            override suspend fun getAdditionalCreditCardTransactions(accountNumber: String): List<Transaction> {
                return listOf()
            }
        }

        val mockedProvider = MockedAccountDataProvider()
        val repository = AccountRepositoryImpl(mockedProvider, testDispatcher)

        // act
        val result = repository.getAccountsAsync().await()

        // assert
        Assert.assertTrue(result is Result.Failure<Account>)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun when_provider_getAccountAsync_returns_list_it_returns_success_result_with_that_list() = runBlocking {
        // arrange
        val account = newAccount("test-account-one", "2323", "100.23", AccountType.CHEQUING)

        class MockedAccountDataProvider : AccountDataProvider {
            override suspend fun getAccounts(): List<Account> {
                return listOf(account)
            }

            override suspend fun getAccountTransactions(accountNumber: String): List<Transaction> {
                return listOf()
            }

            override suspend fun getAdditionalCreditCardTransactions(accountNumber: String): List<Transaction> {
                return listOf()
            }
        }

        val mockedProvider = MockedAccountDataProvider()
        val repository = AccountRepositoryImpl(mockedProvider, testDispatcher)

        // act
        val result = repository.getAccountsAsync().await()

        // assert
        Assert.assertTrue(result is Result.Success<Account>)
        result as Result.Success<Account>
        Assert.assertTrue(result.data.isNotEmpty())
        Assert.assertTrue(result.data.size == 1)
        Assert.assertTrue(result.data[0] == account)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun when_providers_either_getTransaction_methods_succeeds_it_returns_success_result_with_that_list() = runBlocking {
        // arrange
        val account = newAccount("test-account-one", "2323", "100.23", AccountType.CREDIT_CARD)
        val transaction = newTransaction("test-transaction", "23.56", Calendar.getInstance())

        class MockedAccountDataProvider : AccountDataProvider {
            override suspend fun getAccounts(): List<Account> {
                return listOf(account)
            }

            override suspend fun getAccountTransactions(accountNumber: String): List<Transaction> {
                throw IllegalArgumentException()
            }

            override suspend fun getAdditionalCreditCardTransactions(accountNumber: String): List<Transaction> {
                return listOf(transaction)
            }
        }

        val mockedProvider = MockedAccountDataProvider()
        val repository = AccountRepositoryImpl(mockedProvider, testDispatcher)

        // act
        val result = repository.getTransactionsAsync(account.number, account.type).await()

        // assert
        Assert.assertTrue(result is Result.Success<Transaction>)
        result as Result.Success<Transaction>
        Assert.assertTrue(result.data.isNotEmpty())
        Assert.assertTrue(result.data.size == 1)
        Assert.assertTrue(result.data[0] == transaction)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun when_account_type_is_not_credit_card_it_does_not_call_provider_getAdditionalCreditCardTransactions() = runBlocking {
        // arrange
        val account = newAccount("test-account-one", "2323", "100.23", AccountType.MORTGAGE)
        val transaction = newTransaction("test-transaction", "23.56", Calendar.getInstance())

        class MockedAccountDataProvider : AccountDataProvider {
            override suspend fun getAccounts(): List<Account> {
                return listOf(account)
            }

            override suspend fun getAccountTransactions(accountNumber: String): List<Transaction> {
                return listOf(transaction)
            }

            override suspend fun getAdditionalCreditCardTransactions(accountNumber: String): List<Transaction> {
                return listOf(newTransaction("transaction-two", "78.16", Calendar.getInstance()))
            }
        }

        val mockedProvider = MockedAccountDataProvider()
        val repository = AccountRepositoryImpl(mockedProvider, testDispatcher)

        // act
        val result = repository.getTransactionsAsync(account.number, account.type).await()

        // assert
        Assert.assertTrue(result is Result.Success<Transaction>)
        result as Result.Success<Transaction>
        Assert.assertTrue(result.data.isNotEmpty())
        Assert.assertTrue(result.data.size == 1)
        Assert.assertTrue(result.data[0] == transaction)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun when_account_type_is_credit_card_it_calls_both_provider_getTransaction_methods() = runBlocking {
        // arrange
        val account = newAccount("test-account-one", "2323", "100.23", AccountType.CREDIT_CARD)

        val dayOne = Calendar.getInstance()
        dayOne.set(2022, 1, 2, 12, 2)
        val transactionOne = newTransaction("transaction-one", "23.56", dayOne)

        val dayTwo = Calendar.getInstance()
        dayTwo.set(2022, 1, 3, 12, 45)
        val transactionTwo = newTransaction("transaction-two", "78.16", dayTwo)

        class MockedAccountDataProvider : AccountDataProvider {
            override suspend fun getAccounts(): List<Account> {
                return listOf(account)
            }

            override suspend fun getAccountTransactions(accountNumber: String): List<Transaction> {
                return listOf(transactionOne)
            }

            override suspend fun getAdditionalCreditCardTransactions(accountNumber: String): List<Transaction> {
                return listOf(transactionTwo)
            }
        }

        val mockedProvider = MockedAccountDataProvider()
        val repository = AccountRepositoryImpl(mockedProvider, testDispatcher)

        // act
        val result = repository.getTransactionsAsync(account.number, account.type).await()

        // assert
        Assert.assertTrue(result is Result.Success<Transaction>)
        result as Result.Success<Transaction>
        Assert.assertTrue(result.data.isNotEmpty())
        Assert.assertTrue(result.data.size == 2)
        Assert.assertTrue(result.data[0] == transactionTwo)
        Assert.assertTrue(result.data[1] == transactionOne)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun it_sorts_transactions_from_provider_getTransactions_combined_result_descending_by_date() = runBlocking {
        // arrange
        val account = newAccount("test-account-one", "2323", "100.23", AccountType.CREDIT_CARD)

        val dayOne = Calendar.getInstance()
        dayOne.set(2022, 1, 2, 12, 2)
        val transactionOne = newTransaction("transaction-one", "23.56", dayOne)

        val dayTwo = Calendar.getInstance()
        dayTwo.set(2022, 1, 3, 12, 45)
        val transactionTwo = newTransaction("transaction-two", "78.16", dayTwo)

        class MockedAccountDataProvider : AccountDataProvider {
            override suspend fun getAccounts(): List<Account> {
                return listOf(account)
            }

            override suspend fun getAccountTransactions(accountNumber: String): List<Transaction> {
                return listOf(transactionOne)
            }

            override suspend fun getAdditionalCreditCardTransactions(accountNumber: String): List<Transaction> {
                return listOf(transactionTwo)
            }
        }

        val mockedProvider = MockedAccountDataProvider()
        val repository = AccountRepositoryImpl(mockedProvider, testDispatcher)

        // act
        val result = repository.getTransactionsAsync(account.number, account.type).await()

        // assert
        Assert.assertTrue(result is Result.Success<Transaction>)
        result as Result.Success<Transaction>
        Assert.assertTrue(result.data.isNotEmpty())
        Assert.assertTrue(result.data.size == 2)
        Assert.assertTrue(result.data[0] == transactionTwo)
        Assert.assertTrue(result.data[1] == transactionOne)
    }
}