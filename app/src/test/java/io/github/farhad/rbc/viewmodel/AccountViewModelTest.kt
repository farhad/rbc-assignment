package io.github.farhad.rbc.viewmodel

import app.cash.turbine.test
import com.rbc.rbcaccountlibrary.Account
import com.rbc.rbcaccountlibrary.AccountType
import io.github.farhad.rbc.TestUtils
import io.github.farhad.rbc.model.Result
import io.github.farhad.rbc.model.controller.AccountController
import io.github.farhad.rbc.ui.account.list.AccountDataItem
import io.github.farhad.rbc.ui.account.list.AccountsViewModel
import io.github.farhad.rbc.ui.account.list.AccountsViewState
import io.github.farhad.rbc.ui.util.getFriendlyTitle
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class AccountViewModelTest {

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
    fun when_controller_getAccountsAsync_throws_exception_it_emits_error() = runTest {
        // arrange
        class MockedController : AccountController {
            override suspend fun getAccountsAsync(): Deferred<Result<Account>> {
                return coroutineScope {
                    try {
                        withContext(this.coroutineContext) { throw IllegalStateException() }
                    } catch (e: Exception) {
                        return@coroutineScope async { Result.Failure<Account>() }
                    }
                }
            }
        }

        val controller = MockedController()
        val viewModel = AccountsViewModel(controller, testDispatcher)

        // act + assert
        viewModel.accountsViewState.test {
            val result = awaitItem()
            if (result is AccountsViewState.Loading) {
                val secondEmit = awaitItem()
                Assert.assertTrue(secondEmit is AccountsViewState.Error)
            } else {
                Assert.assertTrue(result is AccountsViewState.Error)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun when_controller_getAccountAsync_returns_empty_list_it_emits_emptyResult() = runTest {
        // arrange
        class MockedController : AccountController {
            override suspend fun getAccountsAsync(): Deferred<Result<Account>> {
                return coroutineScope {
                    try {
                        withContext(this.coroutineContext) {
                            return@withContext async { return@async Result.Success<Account>(listOf()) }
                        }
                    } catch (e: Exception) {
                        return@coroutineScope async { Result.Failure<Account>() }
                    }
                }
            }
        }

        val controller = MockedController()
        val viewModel = AccountsViewModel(controller, testDispatcher)

        // act + assert
        viewModel.accountsViewState.test {
            val result = awaitItem()
            if (result is AccountsViewState.Loading) {
                val secondEmit = awaitItem()
                Assert.assertTrue(secondEmit is AccountsViewState.EmptyResult)
            } else {
                Assert.assertTrue(result is AccountsViewState.EmptyResult)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun when_controller_getAccountAsync_returns_list_it_emits_result_with_that_list() = runTest {
        // arrange
        val account = TestUtils.newAccount("test-account-one", "2323", "100.23", AccountType.CHEQUING)

        class MockedController : AccountController {
            override suspend fun getAccountsAsync(): Deferred<Result<Account>> {
                return coroutineScope {
                    try {
                        withContext(this.coroutineContext) {
                            return@withContext async { return@async Result.Success(listOf(account)) }
                        }
                    } catch (e: Exception) {
                        return@coroutineScope async { Result.Failure<Account>() }
                    }
                }
            }
        }

        val controller = MockedController()
        val viewModel = AccountsViewModel(controller, testDispatcher)

        // act + assert
        viewModel.accountsViewState.test {
            val result = awaitItem()
            if (result is AccountsViewState.Loading) {
                val secondEmit = awaitItem()
                Assert.assertTrue(secondEmit is AccountsViewState.Result)

                secondEmit as AccountsViewState.Result
                Assert.assertTrue(secondEmit.dataItems.isNotEmpty())
                Assert.assertTrue(secondEmit.dataItems.size == 2)

                Assert.assertTrue(secondEmit.dataItems[0] is AccountDataItem.Type)
                Assert.assertTrue((secondEmit.dataItems[0] as AccountDataItem.Type).title == account.type.getFriendlyTitle())

                Assert.assertTrue(secondEmit.dataItems[1] is AccountDataItem.Item)
                Assert.assertTrue((secondEmit.dataItems[1] as AccountDataItem.Item).number == account.number)
                Assert.assertTrue((secondEmit.dataItems[1] as AccountDataItem.Item).balance == account.balance)
                Assert.assertTrue((secondEmit.dataItems[1] as AccountDataItem.Item).typeName == (account.type.getFriendlyTitle()))
                Assert.assertTrue((secondEmit.dataItems[1] as AccountDataItem.Item).name == account.name)
            } else {

                result as AccountsViewState.Result
                Assert.assertTrue(result.dataItems.isNotEmpty())
                Assert.assertTrue(result.dataItems.size == 2)

                Assert.assertTrue(result.dataItems[0] is AccountDataItem.Type)
                Assert.assertTrue((result.dataItems[0] as AccountDataItem.Type).title == account.type.getFriendlyTitle())

                Assert.assertTrue(result.dataItems[1] is AccountDataItem.Item)
                Assert.assertTrue((result.dataItems[1] as AccountDataItem.Item).number == account.number)
                Assert.assertTrue((result.dataItems[1] as AccountDataItem.Item).balance == account.balance)
                Assert.assertTrue((result.dataItems[1] as AccountDataItem.Item).typeName == (account.type.getFriendlyTitle()))
                Assert.assertTrue((result.dataItems[1] as AccountDataItem.Item).name == account.name)
            }
        }
    }
}
