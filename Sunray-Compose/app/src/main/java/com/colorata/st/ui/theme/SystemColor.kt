package com.colorata.st.ui.theme

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.util.TypedValue
import android.view.ContextThemeWrapper
import androidx.compose.ui.graphics.Color

//Fun for get Phone THEME
fun color(context: Context): Int {
    val typedValue = TypedValue()
    val contextThemeWrapper = ContextThemeWrapper(
        context,
        android.R.style.Theme_DeviceDefault)
    contextThemeWrapper.theme.resolveAttribute(android.R.attr.colorAccent,
        typedValue, true)
    Log.d("SystemColor", typedValue.data.toString())
    return typedValue.data
}

//Fun for get COLOR THEME BACKGROUND
fun backInt(context: Context): Int {
    val shared: SharedPreferences = context.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
    var color = ""
    when (shared.getBoolean("nightMode", false)) {
        true -> when (color(context)) {
            -5715974 -> color = "162A49"
            -7224321 -> color = "162A49"
            -7686920 -> color = "162A49"
            -4359937 -> color = "2F1845"
            -3625836 -> color = "34271C"
            -3955038 -> color = "543C38"
            -942723 -> color = "4D3830"
            -15007797 -> color = "2C4F47"
            -2629914 -> color = "242527"
            -8076920 -> color = "3D523E"
            -14107177 -> color = "395458"
            -6705972 -> color = "374151"
            -18727 -> color = "#03D46"
            -12722945 -> color = "2B4449"
            -1668371 -> color = "594B5A"
            -4871684 -> color = "3D3953"
            else -> color = "22221A"
        }
        false -> when (color(context)) {
            -5715974 -> color = "8AB4F8"
            -7224321 -> color = "8AB4F8"
            -7686920 -> color = "8AB4F8"
            -4359937 -> color = "C89EF1"
            -3625836 -> color = "C8AC94"
            -3955038 -> color = "C3A6A2"
            -942723 -> color = "E3AF9A"
            -15007797 -> color = "95D4C6"
            -2629914 -> color = "D7DEE6"
            -8076920 -> color = "A1C7A3"
            -14107177 -> color = "91CBD4"
            -6705972 -> color = "A4B4CE"
            -18727 -> color = "FFD6E9"
            -12722945 -> color = "B8F2FF"
            -1668371 -> color = "EAC1ED"
            -4871684 -> color = "C5BBFE"
            else -> color = "CDCDC5"
        }
    }

    val edit = shared.edit()
    edit.putInt("backColor", android.graphics.Color.parseColor("#$color"))
    edit.apply()

    return android.graphics.Color.parseColor("#$color")
}

fun backColor(context: Context) = Color(backInt(context))

//Fun for get COLOR THEME BUTTON
fun buttonInt(context: Context): Int {
    val shared: SharedPreferences = context.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
    var color = "#ffffff"
    when (shared.getBoolean("nightMode", false)) {
        true -> {
            when (color(context)) {
                -5715974 -> color =  "8AB4F8"
                -7224321 -> color = "8AB4F8"
                -7686920 -> color = "8AB4F8"
                -4359937 -> color = "C89EF1"
                -3625836 -> color = "C8AC94"
                -3955038 -> color = "C3A6A2"
                -942723 -> color = "E3AF9A"
                -15007797 -> color = "95D4C6"
                -2629914 -> color = "D7DEE6"
                -8076920 -> color = "A1C7A3"
                -14107177 -> color = "91CBD4"
                -6705972 -> color = "A4B4CE"
                -18727 -> color = "FFD6E9"
                -12722945 -> color = "B8F2FF"
                -1668371 -> color = "EAC1ED"
                -4871684 -> color = "C5BBFE"
                else -> color = "B47C51"
            }
        }
        false -> {
            when (color(context)) {
                -5715974 -> color = "162A49"
                -7224321 -> color = "162A49"
                -7686920 -> color = "162A49"
                -4359937 -> color = "2F1845"
                -3625836 -> color = "34271C"
                -3955038 -> color = "543C38"
                -942723 -> color = "4D3830"
                -15007797 -> color = "2C4F47"
                -2629914 -> color = "242527"
                -8076920 -> color = "3D523E"
                -14107177 -> color = "395458"
                -6705972 -> color = "374151"
                -18727 -> color = "503D46"
                -12722945 -> color = "2B4449"
                -1668371 -> color = "594B5A"
                -4871684 -> color = "3D3953"
                else -> color = "1f2023"
            }
        }
    }

    val edit = shared.edit()
    edit.putInt("buttonColor", android.graphics.Color.parseColor("#$color"))
    edit.apply()

    return android.graphics.Color.parseColor("#$color")
}

fun buttonColor(context: Context) = Color(buttonInt(context))