package com.skillbox.core.extensions

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

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
    AppCompatResources.getDrawable(this, res)
}

fun View.show() = apply { visibility = View.VISIBLE }

fun View.invisible() = apply { visibility = View.INVISIBLE }

fun View.gone() = apply { visibility = View.GONE }

fun View.enable() = apply { isEnabled = true }

fun View.disable() = apply { isEnabled = false }

fun getAbbreviatedFromDateTime(inputDate: String): String {
    //2018-02-20T18:02:13Z
    return try {
        val date = inputDate.split('T')[0].split('-')
        val time = inputDate.split('T')[1].split(':')
        return "${date[2]}.${date[1]}.${date[0]} ${time[0]}:${time[1]}"
    } catch (ex: Exception) {
        Log.e("parseDate", ex.localizedMessage)
        ""
    }
}

fun getDate(inputDate: String): Date {
    //2018-02-20T18:02:13Z
    return try {
        val date = inputDate.split('T')[0].split('-')
        val time = inputDate.split('T')[1].split(':')
        return Date(date[0].toInt()-1900, date[1].toInt(), date[2].toInt(), time[0].toInt(), time[1].toInt())
    } catch (ex: Exception) {
        Log.e("parseDate", ex.localizedMessage)
        Date()
    }
}