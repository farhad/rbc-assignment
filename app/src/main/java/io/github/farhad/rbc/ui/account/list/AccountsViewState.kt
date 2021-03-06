package io.github.farhad.rbc.ui.account.list

import java.io.Serializable

sealed class AccountsViewState(
    val loadingVisible: Boolean,
    val listVisible: Boolean,
    val errorVisible: Boolean,
    val dataItems: List<AccountDataItem>
) {
    class Idle : AccountsViewState(
        loadingVisible = false,
        listVisible = false,
        errorVisible = false,
        dataItems = listOf()
    )

    class Loading : AccountsViewState(
        loadingVisible = true,
        listVisible = false,
        errorVisible = false,
        dataItems = listOf()
    )

    class Result(items: List<AccountDataItem>) : AccountsViewState(
        loadingVisible = false,
        listVisible = true,
        errorVisible = false,
        dataItems = items
    )

    class EmptyResult : AccountsViewState(
        loadingVisible = false,
        listVisible = false,
        errorVisible = false,
        dataItems = listOf()
    )

    class Error : AccountsViewState(
        loadingVisible = false,
        listVisible = false,
        errorVisible = true,
        dataItems = listOf()
    )
}


sealed class AccountDataItem : Serializable {
    data class Type(val title: String) : AccountDataItem()
    data class Item(
        val name: String,
        val number: String,
        val balance: String,
        val currencySymbol: String,
        val typeName: String
    ) : AccountDataItem() {
        val balanceWithCurrencySymbol get() = "$balance $currencySymbol"
    }
}