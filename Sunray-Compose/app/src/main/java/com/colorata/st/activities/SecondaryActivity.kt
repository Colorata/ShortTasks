package com.colorata.st.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.MaterialTheme
import com.colorata.st.CurrentScreen
import com.colorata.st.screens.secondary.BubblePositionSecondary
import com.colorata.st.screens.secondary.BubbleSecondary
import com.colorata.st.screens.secondary.PowerSecondary
import com.colorata.st.ui.theme.Strings
import com.colorata.st.ui.theme.backInt


class SecondaryActivity : AppCompatActivity() {
    @ExperimentalFoundationApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                when(intent.getSerializableExtra(Strings.screen)){
                    CurrentScreen.BUBBLE -> BubbleSecondary()
                    CurrentScreen.POWER -> PowerSecondary()
                    CurrentScreen.POSITION -> BubblePositionSecondary()
                }
            }
        }

        window.navigationBarColor = backInt(this)
        window.statusBarColor = backInt(this)

        
    }
}
