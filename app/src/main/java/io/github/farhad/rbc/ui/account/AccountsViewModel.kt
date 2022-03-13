package io.github.farhad.rbc.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.farhad.rbc.model.AccountController
import io.github.farhad.rbc.ui.navigation.NavigationAction
import io.github.farhad.rbc.ui.util.getFriendlyTitle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*
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
            val accounts = controller.getAccountsAsync().await()
            if (accounts.isEmpty()) {
                _accountsViewState.emit(AccountsViewState.EmptyResult())
            }

            val map = accounts.groupBy { it.type }
            val dataItems = mutableListOf<AccountDataItem>()
            map.keys.forEach { type ->
                dataItems.add(AccountDataItem.Type(title = type.getFriendlyTitle()))
                dataItems.addAll(map[type].orEmpty().map { account ->
                    AccountDataItem.Item(
                        name = account.name,
                        number = account.number,
                        balance = account.balance,
                        currencySymbol = Currency.getInstance(Locale.CANADA).symbol,
                        typeName = account.type.getFriendlyTitle()
                    )
                })
            }

            _accountsViewState.emit(AccountsViewState.Result(items = dataItems))
        }
    }

    fun onAccountsSelected(item: AccountDataItem) {
        item as AccountDataItem.Item

        _navigationAction.value =
            NavigationAction.ShowAccountDetails(item.name, item.number, item.balance, item.typeName)
    }

}



