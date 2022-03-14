package io.github.farhad.rbc.ui.account.detail

sealed class AccountDetailViewState(
    val loadingVisible: Boolean,
    val listVisible: Boolean,
    val errorVisible: Boolean,
    val dataItems: List<AccountDetailsDataItem>
) {

    class Idle : AccountDetailViewState(
        loadingVisible = false,
        listVisible = false,
        errorVisible = false,
        dataItems = listOf()
    )

    class Loading : AccountDetailViewState(
        loadingVisible = true,
        listVisible = false,
        errorVisible = false,
        dataItems = listOf()
    )

    class Result(items: List<AccountDetailsDataItem>) : AccountDetailViewState(
        loadingVisible = false,
        listVisible = true,
        errorVisible = false,
        dataItems = items
    )

    class EmptyResult : AccountDetailViewState(
        loadingVisible = false,
        listVisible = false,
        errorVisible = false,
        dataItems = listOf()
    )

    class Error : AccountDetailViewState(
        loadingVisible = false,
        listVisible = false,
        errorVisible = true,
        dataItems = listOf()
    )
}

sealed class AccountDetailsDataItem {
    data class Date(val formattedDate: String) : AccountDetailsDataItem()
    data class Transaction(val description: String, val amount: String, val currencySymbol: String) :
        AccountDetailsDataItem()
}

sealed class AccountInformationViewState {

    object Idle : AccountInformationViewState()

    data class AccountInformation(
        val name: String,
        val number: String,
        val balance: String,
        val currencySymbol: String
    ) : AccountInformationViewState() {

        val nameAndNumber get() = "$name (${number})"
        val balanceWithCurrencySymbol get() = "$balance $currencySymbol"
    }
}

