package io.github.farhad.rbc.viewmodel

import app.cash.turbine.test
import com.rbc.rbcaccountlibrary.Account
import com.rbc.rbcaccountlibrary.AccountType
import io.github.farhad.rbc.TestUtils
import io.github.farhad.rbc.model.AccountController
import io.github.farhad.rbc.model.Result
import io.github.farhad.rbc.ui.account.list.AccountDataItem
import io.github.farhad.rbc.ui.account.list.AccountsViewModel
import io.github.farhad.rbc.ui.account.list.AccountsViewState
import io.github.farhad.rbc.ui.util.getFriendlyTitle
import kotlinx.coroutines.*
import org.junit.Assert
import org.junit.Test

class AccountViewModelTest {

    @Test
    fun when_controller_getAccountsAsync_throws_exception_it_emits_failure_result() = runBlocking {
        // arrange
        class MockedController : AccountController {
            override suspend fun getAccountsAsync(): Deferred<Result<Account>> {
                return coroutineScope {
                    try {
                        withContext(Dispatchers.IO) {
                            return@withContext async { throw IllegalStateException() }
                        }
                    } catch (e: Exception) {
                        return@coroutineScope async { Result.Failure<Account>() }
                    }
                }
            }
        }

        val controller = MockedController()
        val viewModel = AccountsViewModel(controller, Dispatchers.Default)

        // act + assert
        viewModel.accountsViewState.test {
            val firstEmit = awaitItem()
            Assert.assertTrue(firstEmit is AccountsViewState.Loading)

            val secondItem = awaitItem()
            Assert.assertTrue(secondItem is AccountsViewState.Error)
        }
    }

    @Test
    fun when_controller_getAccountAsync_returns_empty_list_it_emits_emptyResult() = runBlocking(Dispatchers.Default) {
        // arrange
        class MockedController : AccountController {
            override suspend fun getAccountsAsync(): Deferred<Result<Account>> {
                return coroutineScope {
                    try {
                        withContext(Dispatchers.IO) {
                            return@withContext async { return@async Result.Success<Account>(listOf()) }
                        }
                    } catch (e: Exception) {
                        return@coroutineScope async { Result.Failure<Account>() }
                    }
                }
            }
        }

        val controller = MockedController()
        val viewModel = AccountsViewModel(controller, Dispatchers.Default)

        // act + assert
        viewModel.accountsViewState.test {
            val firstEmit = awaitItem()
            Assert.assertTrue(firstEmit is AccountsViewState.Loading)

            val secondItem = awaitItem()
            Assert.assertTrue(secondItem is AccountsViewState.EmptyResult)
        }
    }

    @Test
    fun when_controller_getAccountAsync_returns_list_it_emits_result_with_that_list() = runBlocking(Dispatchers.Default) {
        // arrange
        val account = TestUtils.newAccount("test-account-one", "2323", "100.23", AccountType.CHEQUING)

        class MockedController : AccountController {
            override suspend fun getAccountsAsync(): Deferred<Result<Account>> {
                return coroutineScope {
                    try {
                        withContext(Dispatchers.IO) {
                            return@withContext async { return@async Result.Success(listOf(account)) }
                        }
                    } catch (e: Exception) {
                        return@coroutineScope async { Result.Failure<Account>() }
                    }
                }
            }
        }

        val controller = MockedController()
        val viewModel = AccountsViewModel(controller, Dispatchers.Default)

        // act + assert
        viewModel.accountsViewState.test {
            val firstEmit = awaitItem()
            Assert.assertTrue(firstEmit is AccountsViewState.Loading)

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

        }
    }
}