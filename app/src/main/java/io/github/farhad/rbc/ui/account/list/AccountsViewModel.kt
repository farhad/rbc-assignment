package io.github.farhad.rbc.ui.account.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.farhad.rbc.di.modules.IoDispatcher
import io.github.farhad.rbc.model.Result
import io.github.farhad.rbc.model.controller.AccountController
import io.github.farhad.rbc.ui.navigation.NavigationAction
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AccountsViewModel @Inject constructor(
    private val controller: AccountController,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _navigationAction = MutableLiveData<NavigationAction>()
    val navigationAction: LiveData<NavigationAction> = _navigationAction

    private var _accountsViewState = MutableStateFlow<AccountsViewState>(AccountsViewState.Idle())
    val accountsViewState: StateFlow<AccountsViewState> = _accountsViewState

    init {
        viewModelScope.launch {
            _accountsViewState.emit(AccountsViewState.Loading())
            controller.getAccountsAsync()
                .runCatching { this.await() }
                .onSuccess {
                    when (it) {
                        is Result.Success -> {
                            val viewStates = withContext(ioDispatcher) { it.data.mapToViewDataItems() }
                            if (viewStates.isEmpty()) {
                                _accountsViewState.emit(AccountsViewState.EmptyResult())
                            } else {
                                _accountsViewState.emit(AccountsViewState.Result(items = viewStates))
                            }
                        }

                        else -> {
                            _accountsViewState.emit(AccountsViewState.Error())
                        }
                    }
                }
                .onFailure { _accountsViewState.emit(AccountsViewState.Error()) }
        }
    }

    fun onAccountsSelected(item: AccountDataItem) {
        item as AccountDataItem.Item
        _navigationAction.value = NavigationAction.ShowAccountDetails(item)
    }

}



