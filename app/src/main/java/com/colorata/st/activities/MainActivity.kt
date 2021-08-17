package com.colorata.st.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import com.colorata.st.screens.Navigation
import com.colorata.st.ui.theme.*

class MainActivity : AppCompatActivity() {

    private var nightMode = true

    @ExperimentalMaterialApi
    @ExperimentalComposeUiApi
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

        if (checkCallingOrSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED ||
            Settings.System.canWrite(
                this
            )
        ) SuperStore(this).drop(Strings.isFirst, false)
        else SuperStore(this).drop(Strings.isFirst, true)
        SuperStore(this).drop(Strings.nightMode, nightMode)

        setSystemColor()

        window.navigationBarColor = backgroundInt()
        window.statusBarColor = Color.TRANSPARENT
        window.setDecorFitsSystemWindows(false)
    }
}