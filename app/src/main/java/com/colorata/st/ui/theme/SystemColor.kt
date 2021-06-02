package com.colorata.st.ui.theme

import android.content.Context
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
    return typedValue.data
}

enum class SystemColor(
    val id: Int,
    val primaryHex: String,
    val secondaryHex: String,
    val title: String
) {
    RED(id = 0, primaryHex = "#FFD6E9", secondaryHex = "#503D46", title = Strings.red),
    ORANGE(id = 1, primaryHex = "#E3AF9A", secondaryHex = "#4D3830", title = Strings.orange),
    YELLOW(id = 2, primaryHex = "#C8AC94", secondaryHex = "#34271C", title = Strings.yellow),
    GREEN(id = 3, primaryHex = "#95D4C6", secondaryHex = "#2C4F47", title = Strings.green),
    TURQUOISE(id = 4, primaryHex = "#B8F2FF", secondaryHex = "#2B4449", title = Strings.turquoise),
    BLUE(id = 5, primaryHex = "#8AB4F8", secondaryHex = "#162A49", title = Strings.blue),
    PURPLE(id = 6, primaryHex = "#C5BBFE", secondaryHex = "#3D3953", title = Strings.purple),
    WHITE(id = 7, primaryHex = "#DADCE0", secondaryHex = "#242527", title = Strings.white),
    BLACK(id = 8, primaryHex = "#DADCE0", secondaryHex = "#242527", title = Strings.autoDetect)
}

@Composable
fun getAllColors(): MutableList<Pair<Color, Color>> {
    val final = mutableListOf<Pair<Color, Color>>()
    SystemColor.values().forEach {
        final.add(Pair(Color(it.primaryHex.toIntColor()), Color(it.secondaryHex.toIntColor())))
    }
    return final
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
    return getSharedPreferences(Strings.shared, Context.MODE_PRIVATE).getInt(
        Strings.secondaryInt,
        0
    )
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