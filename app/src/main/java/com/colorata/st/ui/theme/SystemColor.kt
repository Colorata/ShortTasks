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
fun Context.color(): Int {
    val typedValue = TypedValue()
    val contextThemeWrapper = ContextThemeWrapper(
        this,
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
    val controlHex: String,
    val title: String
) {
    BLACK(
        id = 8,
        primaryHex = "#DADCE0",
        secondaryHex = "#242527",
        controlHex = "#4FB5B6B9",
        title = Strings.autoDetect
    ),
    RED(
        id = 0,
        primaryHex = "#FFD6E9",
        secondaryHex = "#503D46",
        controlHex = "#4FC39CAE",
        title = Strings.red
    ),
    ORANGE(
        id = 1,
        primaryHex = "#E3AF9A",
        secondaryHex = "#4D3830",
        controlHex = "#4FC28E7A",
        title = Strings.orange
    ),
    YELLOW(
        id = 2,
        primaryHex = "#C8AC94",
        secondaryHex = "#34271C",
        controlHex = "#4FC39771",
        title = Strings.yellow
    ),
    GREEN(
        id = 3,
        primaryHex = "#95D4C6",
        secondaryHex = "#2C4F47",
        controlHex = "#4F67BCA9",
        title = Strings.green
    ),
    TURQUOISE(
        id = 4,
        primaryHex = "#B8F2FF",
        secondaryHex = "#2B4449",
        controlHex = "#4F65A2AE",
        title = Strings.turquoise
    ),
    BLUE(
        id = 5,
        primaryHex = "#8AB4F8",
        secondaryHex = "#162A49",
        controlHex = "#4F3568B7",
        title = Strings.blue
    ),
    PURPLE(
        id = 6,
        primaryHex = "#C5BBFE",
        secondaryHex = "#3D3953",
        controlHex = "#4F948BC6",
        title = Strings.purple
    ),
    WHITE(
        id = 7,
        primaryHex = "#DADCE0",
        secondaryHex = "#242527",
        controlHex = "#4FB5B6B9",
        title = Strings.white
    )
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
    if (SuperStore(this).catchBoolean(Strings.autoDetect, true)) {
        val h = color().toHex().toHSV().first.toInt()
        val s = color().toHex().toHSV().second.toInt()
        val v = color().toHex().toHSV().third.toInt()

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

        SuperStore(this).drop(
            mutableListOf(
                Pair(
                    Strings.systemColor,
                    color.id
                ),
                Pair(
                    Strings.primaryInt,
                    if (!SuperStore(this).catchBoolean(Strings.nightMode)) color.secondaryHex.toIntColor()
                    else color.primaryHex.toIntColor()
                ),
                Pair(
                    Strings.secondaryInt,
                    if (!SuperStore(this).catchBoolean(Strings.nightMode)) SystemColor.WHITE.primaryHex.toIntColor()
                    else SystemColor.WHITE.secondaryHex.toIntColor()
                ),
                Pair(
                    Strings.controlColor,
                    color.controlHex.toIntColor()
                )
            )
        )
    } else {
        val currentTheme = SystemColor.values().find { it.id == SuperStore(this).catchInt(Strings.systemColor) } ?: SystemColor.BLACK
        SuperStore(this).drop(
            mutableListOf(
                Pair(
                    Strings.systemColor,
                    currentTheme.id
                ),
                Pair(
                    Strings.primaryInt,
                    if (!SuperStore(this).catchBoolean(Strings.nightMode)) currentTheme.secondaryHex.toIntColor()
                    else currentTheme.primaryHex.toIntColor()
                ),
                Pair(
                    Strings.secondaryInt,
                    if (!SuperStore(this).catchBoolean(Strings.nightMode)) SystemColor.WHITE.primaryHex.toIntColor()
                    else SystemColor.WHITE.secondaryHex.toIntColor()
                ),
                Pair(
                    Strings.controlColor,
                    currentTheme.controlHex.toIntColor()
                )
            )
        )
    }

}

fun Context.backgroundInt(): Int =
    SuperStore(this).catchInt(Strings.secondaryInt)


fun Context.foregroundInt(): Int =
    SuperStore(this).catchInt(Strings.primaryInt)

fun Context.backgroundIntControl(): Int =
    SuperStore(this).catchInt(Strings.controlColor)

@Composable
fun backgroundColor(): Color =
    Color(LocalContext.current.backgroundInt())


@Composable
fun foregroundColor(): Color =
    Color(LocalContext.current.foregroundInt())
