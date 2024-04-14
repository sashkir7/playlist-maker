package utils

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE

var View.isVisible: Boolean
    get() = visibility == VISIBLE
    set(value) {
        visibility = if (value) VISIBLE else GONE
    }
