package com.colorata.st.extentions

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.util.TypedValue
import android.view.ContextThemeWrapper
import android.view.View

class GetTheme(private val context: Context) {

    private val shared: SharedPreferences = this.context.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)

    val back = background()
    val button = button()
    val color = color()

    //Fun for get Phone THEME
    private fun color(): Int {
        val typedValue = TypedValue()
        val contextThemeWrapper = ContextThemeWrapper(context,
                android.R.style.Theme_DeviceDefault)
        contextThemeWrapper.theme.resolveAttribute(android.R.attr.colorAccent,
                typedValue, true)
        return typedValue.data
    }

    //Fun for get COLOR THEME BACKGROUND
    private fun background(): Int {
        var color = ""
        when(shared.getBoolean("nightMode", false)){
            true -> {
                when(color()) {
                    -7224321 -> { color = "#162A49" }
                    -7686920 -> { color = "#162A49" }
                    -4359937 -> { color = "#2F1845" }
                    -3625836 -> { color = "#34271C" }
                    -3955038 -> { color = "#543C38" }
                    -942723 -> { color = "#4D3830" }
                    -15007797 -> { color = "#2C4F47" }
                    -2629914 -> { color = "#242527" }
                    -8076920 -> { color = "#3D523E" }
                    -14107177 -> { color = "#395458" }
                    -6705972 -> { color = "#374151" }
                    -18727 -> { color = "#503D46" }
                    -12722945 -> { color = "#2B4449" }
                    -1668371 -> { color = "#594B5A" }
                    -4871684 -> { color = "#3D3953" }
                    else -> { color = "#22221A"}
                }
            } false -> {
            when(color()) {
                -7224321 -> { color = "#8AB4F8" }
                -7686920 -> { color = "#8AB4F8" }
                -4359937 -> { color = "#C89EF1" }
                -3625836 -> { color = "#C8AC94" }
                -3955038 -> { color = "#C3A6A2" }
                -942723 -> { color = "#E3AF9A" }
                -15007797 -> { color = "#95D4C6" }
                -2629914 -> { color = "#D7DEE6" }
                -8076920 -> { color = "#A1C7A3" }
                -14107177 -> { color = "#91CBD4" }
                -6705972 -> { color = "#A4B4CE" }
                -18727 -> { color = "#FFD6E9" }
                -12722945 -> { color = "#B8F2FF" }
                -1668371 -> { color = "#EAC1ED" }
                -4871684 -> { color = "#C5BBFE" }
                else -> { color = "#CDCDC5"}
            }
        }
        }

        val edit = shared.edit()
        edit.putString("backColor", color)
        edit.apply()

        return Color.parseColor(color)
    }

    //Fun for get COLOR THEME BUTTON
    private fun button(): Int {
        var color = "#ffffff"
        when(shared.getBoolean("nightMode", false)){
            true -> {
                when(color()) {
                    -7224321 -> { color = "#8AB4F8" }
                    -7686920 -> { color = "#8AB4F8" }
                    -4359937 -> { color = "#C89EF1" }
                    -3625836 -> { color = "#C8AC94" }
                    -3955038 -> { color = "#C3A6A2" }
                    -942723 -> { color = "#E3AF9A" }
                    -15007797 -> { color = "#95D4C6" }
                    -2629914 -> { color = "#D7DEE6" }
                    -8076920 -> { color = "#A1C7A3" }
                    -14107177 -> { color = "#91CBD4" }
                    -6705972 -> { color = "#A4B4CE" }
                    -18727 -> { color = "#FFD6E9" }
                    -12722945 -> { color = "#B8F2FF" }
                    -1668371 -> { color = "#EAC1ED" }
                    -4871684 -> { color = "#C5BBFE" }
                    else -> { color = "#B47C51"}
                }
            } false -> {
            when(color()) {
                -7224321 -> { color = "#162A49" }
                -7686920 -> { color = "#162A49" }
                -4359937 -> { color = "#2F1845" }
                -3625836 -> { color = "#34271C" }
                -3955038 -> { color = "#543C38" }
                -942723 -> { color = "#4D3830" }
                -15007797 -> { color = "#2C4F47" }
                -2629914 -> { color = "#242527" }
                -8076920 -> { color = "#3D523E" }
                -14107177 -> { color = "#395458" }
                -6705972 -> { color = "#374151" }
                -18727 -> { color = "#503D46" }
                -12722945 -> { color = "#2B4449" }
                -1668371 -> { color = "#594B5A" }
                -4871684 -> { color = "#3D3953" }
                else -> { color = "#1f2023"}
            }
        }
        }

        val edit = shared.edit()
        edit.putString("buttonColor", color)
        edit.apply()

        return Color.parseColor(color)
    }

    //Fun for CONFIGURING BACKGROUND
    fun confBack(dialogLayout: View){
        when(shared.getBoolean("nightMode", false)){
            false -> {
                dialogLayout.setBackgroundColor(background())
            }
            true -> {
                dialogLayout.setBackgroundColor(background())
            }
        }
        dialogLayout.rootView.fitsSystemWindows = true
    }
}