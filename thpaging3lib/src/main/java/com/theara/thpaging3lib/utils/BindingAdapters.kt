package com.theara.thpaging3lib.utils

import android.view.View
import androidx.databinding.BindingAdapter

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("visibleGone")
    fun visibleGone(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }
}