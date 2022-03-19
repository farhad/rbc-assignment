package io.github.farhad.rbc.ui.account.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.farhad.rbc.di.modules.IoDispatcher
import io.github.farhad.rbc.model.AccountDetailController
import io.github.farhad.rbc.model.Result
import io.github.farhad.rbc.ui.util.fromFriendlyTitle
import io.github.farhad.rbc.ui.util.stringOrEmpty
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class AccountDetailsViewModel @Inject constructor(
    private val controller: AccountDetailController,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private var _accountInformation = MutableStateFlow<AccountInformationViewState>(AccountInformationViewState.Idle)
    val accountInformation: StateFlow<AccountInformationViewState> = _accountInformation

    private var _accountDetails = MutableStateFlow<AccountDetailViewState>(AccountDetailViewState.Idle())
    val accountDetails: StateFlow<AccountDetailViewState> = _accountDetails

    fun setUp(name: String?, number: String?, balance: String?, typeName: String?) {
        val accountType = fromFriendlyTitle(typeName.stringOrEmpty())
        val accountNumber = number.stringOrEmpty()

        viewModelScope.launch {
            _accountInformation.emit(
                AccountInformationViewState.AccountInformation(
                    name.stringOrEmpty(),
                    accountNumber,
                    balance.stringOrEmpty(),
                    Currency.getInstance(Locale.CANADA).symbol
                )
            )
        }

        viewModelScope.launch {
            _accountDetails.emit(AccountDetailViewState.Loading())

            controller.getTransactionsAsync(accountNumber, accountType)
                .runCatching { this.await() }
                .onSuccess {
                    when (it) {
                        is Result.Success -> {
                            val viewState = withContext(ioDispatcher) { it.data.mapToViewDataItems() }
                            if (viewState.isEmpty()) {
                                _accountDetails.emit(AccountDetailViewState.EmptyResult())
                            } else {
                                _accountDetails.emit(AccountDetailViewState.Result(viewState))
                            }
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
