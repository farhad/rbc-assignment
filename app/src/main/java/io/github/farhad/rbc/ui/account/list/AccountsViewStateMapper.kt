package io.github.farhad.rbc.ui.account.list

import com.rbc.rbcaccountlibrary.Account
import io.github.farhad.rbc.ui.util.getFriendlyTitle
import java.util.*

fun List<Account>.mapToViewDataItems(): List<AccountDataItem> {
    val map = this.groupBy { it.type }
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

    return dataItems
}