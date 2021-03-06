package io.github.farhad.rbc.ui.util

import android.view.View
import com.rbc.rbcaccountlibrary.AccountType
import java.text.DateFormatSymbols
import java.util.*

fun AccountType.getFriendlyTitle(): String {
    return name.replace("_", " ")
        .lowercase()
        .split(" ")
        .joinToString(" ") { it.capitalizeFirstLetter() }.trimEnd()

}

fun String.capitalizeFirstLetter(): String {
    return replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.getDefault())
        else it.toString()
    }
}

fun View.changeVisibility(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

fun String?.isNotNullOrEmpty(): Boolean {
    return when {
        this == "null" -> false
        this == null -> false
        this == "" -> false
        else -> true
    }
}

fun String?.stringOrEmpty(): String {
    return if (isNotNullOrEmpty()) this.toString() else ""
}

fun fromFriendlyTitle(friendlyTitle: String): AccountType {
    return if (friendlyTitle == AccountType.CREDIT_CARD.getFriendlyTitle())
        AccountType.CREDIT_CARD
    else AccountType.valueOf(friendlyTitle.uppercase(Locale.ROOT))
}

fun formatDate(calendar: Calendar): String {
    val dayName = DateFormatSymbols.getInstance().shortWeekdays[calendar.get(Calendar.DAY_OF_WEEK)]
    val month = DateFormatSymbols.getInstance().shortMonths[calendar[Calendar.MONTH]]
    val day = calendar[Calendar.DAY_OF_MONTH]
    val year = calendar[Calendar.YEAR]
    return "$dayName, $day $month, $year"
}