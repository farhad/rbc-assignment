package io.github.farhad.rbc.ui.account.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.farhad.rbc.model.AccountDetailController
import io.github.farhad.rbc.model.Result
import io.github.farhad.rbc.ui.util.fromFriendlyTitle
import io.github.farhad.rbc.ui.util.stringOrEmpty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class AccountDetailViewModel @Inject constructor(private val controller: AccountDetailController) : ViewModel() {

    private var _accountInformation = MutableStateFlow<AccountInformationViewState>(AccountInformationViewState.Idle)
    val accountInformation: StateFlow<AccountInformationViewState> = _accountInformation

    private var _accountDetails = MutableStateFlow<AccountDetailViewState>(AccountDetailViewState.Idle())
    val accountDetails: StateFlow<AccountDetailViewState> = _accountDetails

    fun setUp(name: String?, number: String?, balance: String?, typeName: String?) {
        val accountName = name.stringOrEmpty()
        val accountType = fromFriendlyTitle(typeName.stringOrEmpty())
        val accountNumber = number.stringOrEmpty()
        val accountBalance = balance.stringOrEmpty()

        viewModelScope.launch {
            _accountInformation.emit(
                AccountInformationViewState.AccountInformation(
                    accountName,
                    accountNumber,
                    accountBalance,
                    Currency.getInstance(Locale.CANADA).symbol
                )
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            _accountDetails.emit(AccountDetailViewState.Loading())

            controller.getTransactionsAsync(accountName, accountType)
                .runCatching { this.await() }
                .onSuccess {
                    when (it) {
                        is Result.Success -> {
                            _accountDetails.emit(AccountDetailViewState.Result(it.data.mapToViewState()))
                        }
                        else -> {
                            _accountDetails.emit(AccountDetailViewState.Error())
                        }
                    }
                }.onFailure {
                    _accountDetails.emit(AccountDetailViewState.Error())
                }
        }
    }
}
