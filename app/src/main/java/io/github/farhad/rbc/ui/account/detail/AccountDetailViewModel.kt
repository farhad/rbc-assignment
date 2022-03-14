package io.github.farhad.rbc.ui.account.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.farhad.rbc.model.AccountDetailController
import io.github.farhad.rbc.ui.util.formatDate
import io.github.farhad.rbc.ui.util.fromFriendlyTitle
import io.github.farhad.rbc.ui.util.isNotNullOrEmpty
import io.github.farhad.rbc.ui.util.stringOrEmpty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class AccountDetailViewModel @Inject constructor(private val controller: AccountDetailController) : ViewModel() {

    private var _accountInformation = MutableStateFlow<AccountInformationViewState>(AccountInformationViewState.Idle())
    val accountInformation: StateFlow<AccountInformationViewState> = _accountInformation

    private var _accountDetails = MutableStateFlow<AccountDetailViewState>(AccountDetailViewState.Idle())
    val accountDetails: StateFlow<AccountDetailViewState> = _accountDetails

    private lateinit var accountNumber: String
    private lateinit var accountName: String
    private lateinit var accountBalance: String
    private lateinit var accountTypeName: String

    fun setUp(name: String?, number: String?, balance: String?, typeName: String?) {
        this.accountNumber = number.stringOrEmpty()
        this.accountName = name.stringOrEmpty()
        this.accountBalance = balance.stringOrEmpty()
        this.accountTypeName = typeName.stringOrEmpty()

        if (validateSetUpParameters(accountName, accountNumber, accountBalance, accountTypeName)) {
            viewModelScope.launch(Dispatchers.IO) {
                _accountInformation.emit(
                    AccountInformationViewState.AccountInformation(
                        accountName,
                        accountNumber,
                        accountBalance,
                        Currency.getInstance(Locale.CANADA).symbol
                    )
                )
                _accountDetails.emit(AccountDetailViewState.Loading())
                controller.getTransactionsAsync(accountNumber, fromFriendlyTitle(accountTypeName)).runCatching {
                    this.await()
                }.onSuccess { transactions ->
                    val map = transactions.groupBy { formatDate(it.date as GregorianCalendar) }
                    val dataItems = mutableListOf<AccountDetailsDataItem>()
                    map.keys.forEach {
                        dataItems.add(AccountDetailsDataItem.Date(it))
                        dataItems.addAll(map[it].orEmpty().map { trx ->
                            AccountDetailsDataItem.Transaction(
                                description = trx.description,
                                amount = trx.amount,
                                currencySymbol = Currency.getInstance(Locale.CANADA).symbol
                            )
                        })

                        _accountDetails.emit(AccountDetailViewState.Result(dataItems))
                    }
                }.onFailure {
                    _accountDetails.emit(AccountDetailViewState.Error())
                }
            }
        }
    }

    // todo : move validation to controller
    private fun validateSetUpParameters(name: String?, number: String?, balance: String?, typeName: String?): Boolean {
        return name.isNotNullOrEmpty() && number.isNotNullOrEmpty() && balance.isNotNullOrEmpty() && typeName.isNotNullOrEmpty()
    }

}