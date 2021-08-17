package com.colorata.st.activities

import android.graphics.Color
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.ExperimentalComposeUiApi
import com.colorata.st.CurrentScreen
import com.colorata.st.screens.secondary.AddAppSecondary
import com.colorata.st.screens.secondary.PowerControlsSecondary
import com.colorata.st.screens.secondary.PowerSecondary
import com.colorata.st.screens.secondary.ThemePickerSecondary
import com.colorata.st.ui.theme.Strings
import com.colorata.st.ui.theme.backgroundInt


class SecondaryActivity : AppCompatActivity() {
    @ExperimentalComposeUiApi
    @ExperimentalMaterialApi
    @ExperimentalFoundationApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                when (intent.getSerializableExtra(Strings.screen)) {
                    CurrentScreen.POWER -> PowerSecondary()
                    CurrentScreen.ADD_APP -> AddAppSecondary()
                    CurrentScreen.THEME_PICKER -> ThemePickerSecondary()
                    CurrentScreen.POWER_CONTROLS -> PowerControlsSecondary()
                }
            }
        }
        if (intent.getSerializableExtra(Strings.screen) == CurrentScreen.POWER_CONTROLS) {
            window.navigationBarColor = Color.TRANSPARENT
            window.statusBarColor = Color.TRANSPARENT
            window.setDecorFitsSystemWindows(false)
        } else {
            window.navigationBarColor = backgroundInt()
            window.statusBarColor = backgroundInt()
        }
    }

}

