package io.github.farhad.rbc.ui.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.farhad.rbc.model.AccountsController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class AccountsViewModel @Inject constructor(private val controller: AccountsController) :
    ViewModel() {

    private var _accountsViewState: MutableStateFlow<AccountsViewState> =
        MutableStateFlow(AccountsViewState.Idle())
    val accountsViewState: StateFlow<AccountsViewState> = _accountsViewState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _accountsViewState.emit(AccountsViewState.Loading())
            delay(1500)
            controller.getAccounts()
                .catch { _accountsViewState.emit(AccountsViewState.Error()) }
                .collect { accounts ->
                    if (accounts.isEmpty()) {
                        _accountsViewState.emit(AccountsViewState.EmptyResult())
                        return@collect
                    }

                    val map = accounts.groupBy { it.type }
                    val dataItems = mutableListOf<AccountDataItem>()
                    map.keys.forEach { type ->
                        dataItems.add(AccountDataItem.Type(title = type.name))
                        dataItems.addAll(map[type].orEmpty().map { account ->
                            AccountDataItem.Item(
                                name = account.name,
                                number = account.number,
                                balance = account.balance,
                                currencySymbol = Currency.getInstance(Locale.CANADA).symbol
                            )
                        })
                    }

                    _accountsViewState.emit(AccountsViewState.Result(items = dataItems))
                }
        }
    }

    fun onAccountsSelected(item: AccountDataItem) {
    }

}



