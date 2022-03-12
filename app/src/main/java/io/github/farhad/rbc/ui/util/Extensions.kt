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