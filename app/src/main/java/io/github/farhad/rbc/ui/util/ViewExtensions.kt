package io.github.farhad.rbc.ui.util

import android.view.View

fun View.changeVisibility(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}