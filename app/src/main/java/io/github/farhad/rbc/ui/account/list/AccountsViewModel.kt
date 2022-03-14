package io.github.farhad.rbc.ui.account.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.farhad.rbc.model.AccountController
import io.github.farhad.rbc.model.Result
import io.github.farhad.rbc.ui.navigation.NavigationAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AccountsViewModel @Inject constructor(private val controller: AccountController) :
    ViewModel() {

    private val _navigationAction = MutableLiveData<NavigationAction>()
    val navigationAction: LiveData<NavigationAction> = _navigationAction

    private var _accountsViewState = MutableStateFlow<AccountsViewState>(AccountsViewState.Idle())
    val accountsViewState: StateFlow<AccountsViewState> = _accountsViewState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _accountsViewState.emit(AccountsViewState.Loading())

            controller.getAccountsAsync()
                .runCatching { this.await() }
                .onSuccess {
                    when (it) {
                        is Result.Success -> {
                            _accountsViewState.emit(AccountsViewState.Result(items = it.data.mapToViewState()))
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

        _navigationAction.value = NavigationAction.ShowAccountDetails(item.name, item.number, item.balance, item.typeName)
    }

}


