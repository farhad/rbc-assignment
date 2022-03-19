package io.github.farhad.rbc.viewmodel

import app.cash.turbine.test
import com.rbc.rbcaccountlibrary.AccountType
import com.rbc.rbcaccountlibrary.Transaction
import io.github.farhad.rbc.TestUtils
import io.github.farhad.rbc.model.AccountDetailController
import io.github.farhad.rbc.model.Result
import io.github.farhad.rbc.ui.account.detail.AccountDetailViewState
import io.github.farhad.rbc.ui.account.detail.AccountDetailsDataItem
import io.github.farhad.rbc.ui.account.detail.AccountDetailsViewModel
import io.github.farhad.rbc.ui.account.detail.AccountInformationViewState
import io.github.farhad.rbc.ui.util.formatDate
import io.github.farhad.rbc.ui.util.getFriendlyTitle
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.After
import org.junit.Assert
import org.junit.Test
import java.util.*

class AccountDetailsViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    val testDispatcher = TestCoroutineDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun after() {
        testDispatcher.cancelChildren()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun when_setUp_is_called_it_emits_account_information_viewstate() = runBlocking {
        // arrange
        val account = TestUtils.newAccount("CHQ", "1222", "41.02", AccountType.CHEQUING)

        class MockedController : AccountDetailController {
            override suspend fun getTransactionsAsync(accountNumber: String, accountType: AccountType): Deferred<Result<Transaction>> {
                return coroutineScope {
                    try {
                        withContext(Dispatchers.IO) {
                            return@withContext async { throw IllegalStateException() }
                        }
                    } catch (e: Exception) {
                        return@coroutineScope async { Result.Failure<Transaction>() }
                    }
                }
            }
        }

        // act
        val controller = MockedController()
        val viewModel = AccountDetailsViewModel(controller, testDispatcher)
        viewModel.setUp(account.name, account.number, account.balance, account.type.getFriendlyTitle())

        // assert

        viewModel.accountInformation.test {
            val result = awaitItem()
            Assert.assertTrue(result is AccountInformationViewState.AccountInformation)
            result as AccountInformationViewState.AccountInformation
            Assert.assertTrue(result.name == account.name)
            Assert.assertTrue(result.balance == account.balance)
            Assert.assertTrue(result.number == account.number)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun when_controller_getTransactionsAsync_throws_exception_it_emits_error() = runBlocking {
        // arrange
        val account = TestUtils.newAccount("CHQ", "1222", "41.02", AccountType.CHEQUING)

        class MockedController : AccountDetailController {
            override suspend fun getTransactionsAsync(accountNumber: String, accountType: AccountType): Deferred<Result<Transaction>> {
                return coroutineScope {
                    try {
                        withContext(Dispatchers.IO) {
                            return@withContext async { throw IllegalStateException() }
                        }
                    } catch (e: Exception) {
                        return@coroutineScope async { Result.Failure<Transaction>() }
                    }
                }
            }
        }

        // act
        val controller = MockedController()
        val viewModel = AccountDetailsViewModel(controller, testDispatcher)
        viewModel.setUp(account.name, account.number, account.balance, account.type.getFriendlyTitle())

        // assert
        viewModel.accountDetails.test {
            val result = awaitItem()
            if (result is AccountDetailViewState.Loading) {
                val secondEmit = awaitItem()
                Assert.assertTrue(secondEmit is AccountDetailViewState.Error)
            } else {
                Assert.assertTrue(result is AccountDetailViewState.Error)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun when_controller_getTransactionsAsync_returns_empty_list_it_emits_emptyResult() = runBlocking {
        // arrange
        val account = TestUtils.newAccount("CHQ", "1222", "41.02", AccountType.CHEQUING)

        class MockedController : AccountDetailController {
            override suspend fun getTransactionsAsync(accountNumber: String, accountType: AccountType): Deferred<Result<Transaction>> {
                return coroutineScope {
                    try {
                        withContext(Dispatchers.IO) {
                            return@withContext async { return@async Result.Success(listOf<Transaction>()) }
                        }
                    } catch (e: Exception) {
                        return@coroutineScope async { Result.Failure<Transaction>() }
                    }
                }
            }
        }

        // act
        val controller = MockedController()
        val viewModel = AccountDetailsViewModel(controller, testDispatcher)
        viewModel.setUp(account.name, account.number, account.balance, account.type.getFriendlyTitle())

        // assert
        viewModel.accountDetails.test {
            val result = awaitItem()
            if (result is AccountDetailViewState.Loading) {
                val secondEmit = awaitItem()
                Assert.assertTrue(secondEmit is AccountDetailViewState.EmptyResult)
            } else {
                Assert.assertTrue(result is AccountDetailViewState.EmptyResult)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun when_controller_getTransactionsAsync_returns_list_it_emits_results_with_that_list() = runBlocking {
        // arrange
        val account = TestUtils.newAccount("CHQ", "1222", "41.02", AccountType.CHEQUING)
        val dateOne = Calendar.getInstance()
        dateOne.set(2021, 12, 14)
        val transaction = TestUtils.newTransaction("BILL", "21.02", dateOne)

        class MockedController : AccountDetailController {
            override suspend fun getTransactionsAsync(accountNumber: String, accountType: AccountType): Deferred<Result<Transaction>> {
                return coroutineScope {
                    try {
                        withContext(Dispatchers.IO) {
                            return@withContext async { return@async Result.Success(listOf(transaction)) }
                        }
                    } catch (e: Exception) {
                        return@coroutineScope async { Result.Failure<Transaction>() }
                    }
                }
            }
        }

        // act
        val controller = MockedController()
        val viewModel = AccountDetailsViewModel(controller, testDispatcher)
        viewModel.setUp(account.name, account.number, account.balance, account.type.getFriendlyTitle())

        // assert
        viewModel.accountDetails.test {
            val result = awaitItem()
            if (result is AccountDetailViewState.Loading) {
                val secondEmit = awaitItem()

                Assert.assertTrue(secondEmit is AccountDetailViewState.Result)

                secondEmit as AccountDetailViewState.Result
                Assert.assertTrue(secondEmit.dataItems.isNotEmpty())
                Assert.assertTrue(secondEmit.dataItems.size == 2)
                Assert.assertTrue(secondEmit.dataItems[0] is AccountDetailsDataItem.Date)
                Assert.assertTrue((secondEmit.dataItems[0] as AccountDetailsDataItem.Date).formattedDate == formatDate(dateOne))

                Assert.assertTrue(secondEmit.dataItems[1] is AccountDetailsDataItem.Transaction)
                Assert.assertTrue((secondEmit.dataItems[1] as AccountDetailsDataItem.Transaction).amount == transaction.amount)
                Assert.assertTrue((secondEmit.dataItems[1] as AccountDetailsDataItem.Transaction).description == transaction.description)

            } else {
                Assert.assertTrue(result is AccountDetailViewState.Result)

                result as AccountDetailViewState.Result
                Assert.assertTrue(result.dataItems.isNotEmpty())
                Assert.assertTrue(result.dataItems.size == 2)
                Assert.assertTrue(result.dataItems[0] is AccountDetailsDataItem.Date)
                Assert.assertTrue((result.dataItems[0] as AccountDetailsDataItem.Date).formattedDate == formatDate(dateOne))

                Assert.assertTrue(result.dataItems[1] is AccountDetailsDataItem.Transaction)
                Assert.assertTrue((result.dataItems[1] as AccountDetailsDataItem.Transaction).amount == transaction.amount)
                Assert.assertTrue((result.dataItems[1] as AccountDetailsDataItem.Transaction).description == transaction.description)
            }
        }
    }
}