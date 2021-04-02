package com.colorata.st

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import com.colorata.st.screens.secondary.BubbleSecondary
import com.colorata.st.screens.secondary.PowerSecondary
import com.colorata.st.ui.theme.backInt


class SecondaryActivity : AppCompatActivity() {
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                when(intent.getSerializableExtra("Screen")){
                    CurrentScreen.BUBBLE -> BubbleSecondary()
                    CurrentScreen.POWER -> PowerSecondary()
                }
            }
        }

        window.navigationBarColor = backInt(this)
        window.statusBarColor = backInt(this)

        
    }
}

