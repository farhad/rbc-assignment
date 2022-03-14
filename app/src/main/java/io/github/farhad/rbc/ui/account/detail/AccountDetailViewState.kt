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

sealed class AccountInformationViewState(
    open val name: String,
    open val number: String,
    open val balance: String,
    open val currencySymbol: String
) {
    class Idle : AccountInformationViewState("", "", "", "")

    data class AccountInformation(
        override val name: String,
        override val number: String,
        override val balance: String,
        override val currencySymbol: String
    ) : AccountInformationViewState(name, number, balance, currencySymbol)
}

