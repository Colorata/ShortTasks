package com.colorata.st.ui.theme

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.util.TypedValue
import android.view.ContextThemeWrapper
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.colorata.st.extensions.toHSV
import com.colorata.st.extensions.toHex

fun String.toIntColor(): Int = android.graphics.Color.parseColor(this)


//Fun for get Phone THEME
fun color(context: Context): Int {
    val typedValue = TypedValue()
    val contextThemeWrapper = ContextThemeWrapper(
        context,
        android.R.style.Theme_DeviceDefault
    )
    contextThemeWrapper.theme.resolveAttribute(
        android.R.attr.colorAccent,
        typedValue, true
    )
    Log.d("Theme", typedValue.data.toString())
    return typedValue.data
}

//Fun for get COLOR THEME BACKGROUND
fun backInt(context: Context): Int {
    val shared: SharedPreferences =
        context.getSharedPreferences(Strings.shared, Context.MODE_PRIVATE)
    var color = ""
    when (shared.getBoolean(Strings.nightMode, false)) {
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
            -18727 -> color = "503D46"
            -12722945 -> color = "2B4449"
            -1668371 -> color = "594B5A"
            -4871684 -> color = "3D3953"
            else -> color = "162A49"
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
            else -> color = "8AB4F8"
        }
    }

    return android.graphics.Color.parseColor("#$color")
}

fun backIntControl(context: Context): Int {
    val color: String
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
        else -> color = "162A49"
    }

    return android.graphics.Color.parseColor("#$color")
}

fun backColor(context: Context) = Color(backInt(context))

//Fun for get COLOR THEME BUTTON
fun buttonInt(context: Context): Int {
    val shared: SharedPreferences =
        context.getSharedPreferences(Strings.shared, Context.MODE_PRIVATE)
    var color = "#ffffff"
    when (shared.getBoolean(Strings.nightMode, false)) {
        true -> {
            when (color(context)) {
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
                else -> color = "8AB4F8"
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
                else -> color = "162A49"
            }
        }
    }

    return android.graphics.Color.parseColor("#$color")
}

fun buttonColor(context: Context) = Color(buttonInt(context))

enum class SystemColor(
    val id: Int,
    val primaryHex: String,
    val secondaryHex: String
) {
    RED(id = 0, primaryHex = "#FFD6E9", secondaryHex = "#503D46"),
    ORANGE(id = 1, primaryHex = "#E3AF9A", secondaryHex = "#4D3830"),
    YELLOW(id = 2, primaryHex = "#C8AC94", secondaryHex = "#34271C"),
    GREEN(id = 3, primaryHex = "#95D4C6", secondaryHex = "#2C4F47"),
    TURQUOISE(id = 4, primaryHex = "#B8F2FF", secondaryHex = "#2B4449"),
    BLUE(id = 5, primaryHex = "#8AB4F8", secondaryHex = "#162A49"),
    PURPLE(id = 6, primaryHex = "#C5BBFE", secondaryHex = "#3D3953"),
    WHITE(id = 7, primaryHex = "#DADCE0", secondaryHex = "#242527"),
    BLACK(id = 8, primaryHex = "#DADCE0", secondaryHex = "#242527")
}

fun Context.setSystemColor() {
    val h = color(this).toHex().toHSV().first.toInt()
    val s = color(this).toHex().toHSV().second.toInt()
    val v = color(this).toHex().toHSV().third.toInt()

    val shared = getSharedPreferences(Strings.shared, Context.MODE_PRIVATE)

    val color = if (v <= 30) {
        SystemColor.BLACK
    } else if (v >= 35 && s >= 30 && h <= 13) {
        SystemColor.RED
    } else if (v >= 35 && s >= 30 && h >= 286) {
        SystemColor.RED
    } else if (v >= 35 && s >= 30 && h >= 14 && h <= 43) {
        SystemColor.ORANGE
    } else if (v >= 35 && s >= 30 && h >= 44 && h <= 60) {
        SystemColor.YELLOW
    } else if (v >= 35 && s >= 30 && h >= 61 && h <= 160) {
        SystemColor.GREEN
    } else if (v >= 35 && s >= 30 && h >= 161 && h <= 185) {
        SystemColor.TURQUOISE
    } else if (v >= 35 && s >= 30 && h >= 186 && h <= 250) {
        SystemColor.BLUE
    } else if (v >= 35 && s >= 30 && h >= 251 && h <= 285) {
        SystemColor.PURPLE
    } else if (v >= 35 && s <= 40) {
        SystemColor.WHITE
    } else SystemColor.BLACK

    val edit = shared.edit()
    when (shared.getBoolean(Strings.nightMode, false)) {
        true -> {
            edit.putInt(Strings.systemColor, color.id)
            edit.putInt(Strings.primaryInt, color.primaryHex.toIntColor())
            edit.putInt(Strings.secondaryInt, color.secondaryHex.toIntColor())
            edit.putInt(Strings.controlColor, color.secondaryHex.toIntColor())
            edit.apply()
        }
        false -> {
            edit.putInt(Strings.systemColor, color.id)
            edit.putInt(Strings.primaryInt, color.secondaryHex.toIntColor())
            edit.putInt(Strings.secondaryInt, color.primaryHex.toIntColor())
            edit.putInt(Strings.controlColor, color.secondaryHex.toIntColor())
            edit.apply()
        }
    }

}

fun Context.backgroundInt(): Int {
    return getSharedPreferences(Strings.shared, Context.MODE_PRIVATE).getInt(Strings.secondaryInt, 0)
}

fun Context.foregroundInt(): Int {
    return getSharedPreferences(Strings.shared, Context.MODE_PRIVATE).getInt(Strings.primaryInt, 0)
}

fun Context.backgroundIntControl(): Int {
    val shared = getSharedPreferences(Strings.shared, Context.MODE_PRIVATE)
    return shared.getInt(Strings.controlColor, 0)
}

@Composable
fun backgroundColor(): Color {
    return Color(LocalContext.current.backgroundInt())
}

@Composable
fun foregroundColor(): Color {
    return Color(LocalContext.current.foregroundInt())
}