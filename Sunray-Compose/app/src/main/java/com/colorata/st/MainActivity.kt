package com.colorata.st

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
import androidx.compose.ui.platform.LocalContext
import com.colorata.st.ui.theme.backColor
import com.colorata.st.ui.theme.backInt

class MainActivity : AppCompatActivity() {

    private var nightMode = true

    @ExperimentalAnimationApi
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Box(modifier = Modifier.background(color = backColor(LocalContext.current))) {
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

        val shared: SharedPreferences = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        shared.edit().putBoolean("nightMode", nightMode).apply()

        window.navigationBarColor = backInt(this)
        window.statusBarColor = Color.TRANSPARENT
        window.setDecorFitsSystemWindows(false)

    }
}
