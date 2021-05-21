package com.colorata.st.activities

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import com.colorata.st.screens.Navigation
import com.colorata.st.ui.theme.Strings
import com.colorata.st.ui.theme.backgroundColor
import com.colorata.st.ui.theme.backgroundInt
import com.colorata.st.ui.theme.setSystemColor

class MainActivity : AppCompatActivity() {

    private var nightMode = true

    @ExperimentalAnimationApi
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Box(modifier = Modifier.background(color = backgroundColor())) {
                    Navigation()
                }
            }
        }

        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> {
                nightMode = false
            }
            Configuration.UI_MODE_NIGHT_YES -> {
                nightMode = true
            }
        }

        val shared: SharedPreferences = getSharedPreferences(Strings.shared, Context.MODE_PRIVATE)
        shared.edit().putBoolean(Strings.nightMode, nightMode).apply()

        setSystemColor()

        window.navigationBarColor = backgroundInt()
        window.statusBarColor = Color.TRANSPARENT
        window.setDecorFitsSystemWindows(false)
    }
}
