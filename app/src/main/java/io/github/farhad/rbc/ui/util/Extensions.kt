package io.github.farhad.rbc.ui.util

import android.view.View
import com.rbc.rbcaccountlibrary.AccountType
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