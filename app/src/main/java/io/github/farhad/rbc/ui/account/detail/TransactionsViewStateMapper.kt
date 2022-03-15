package io.github.farhad.rbc.ui.account.detail

import com.rbc.rbcaccountlibrary.Transaction
import io.github.farhad.rbc.ui.util.formatDate
import java.util.*

fun List<Transaction>.mapToViewDataItems(): List<AccountDetailsDataItem> {
    val dataItems = mutableListOf<AccountDetailsDataItem>()
    val map = this.groupBy { formatDate(it.date) }

    map.keys.forEach {
        dataItems.add(AccountDetailsDataItem.Date(it))
        dataItems.addAll(map[it].orEmpty().map { trx ->
            AccountDetailsDataItem.Transaction(
                description = trx.description,
                amount = trx.amount,
                currencySymbol = Currency.getInstance(Locale.CANADA).symbol
            )
        })
    }

    return dataItems
}