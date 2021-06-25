package com.skillbox.core.extensions

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun <T: Fragment> T.withArguments(action: Bundle.() -> Unit): T {
    return apply {
        arguments = Bundle().apply(action)
    }
}

fun Context.checkDarkTheme() : Boolean {
    return when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
        Configuration.UI_MODE_NIGHT_NO -> false
        Configuration.UI_MODE_NIGHT_YES -> true
        else -> false
    }
}

fun Context.getDrawable(@DrawableRes res: Int) {
    ContextCompat.getDrawable(this, res)
}

fun View.show() = run { visibility = View.VISIBLE }

fun View.invisible() = run { visibility = View.INVISIBLE }

fun View.gone() = run { visibility = View.GONE }

fun View.enable() = run { isEnabled = true }

fun View.disable() = run { isEnabled = false }