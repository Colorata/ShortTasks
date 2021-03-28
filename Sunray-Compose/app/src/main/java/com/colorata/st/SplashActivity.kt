package com.colorata.st

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.Window
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.colorata.st.ui.theme.*
import java.util.*
import kotlin.concurrent.schedule

class SplashActivity : AppCompatActivity() {

    private var nightMode = true

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Splash()
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
        window.statusBarColor = backInt(this)

        window.setDecorFitsSystemWindows(false)

        Timer().schedule(2000){
            val intent = Intent(this@SplashActivity, MainActivity::class.java)

            startActivity(intent)
            finish()
        }
    }
}

@ExperimentalAnimationApi
@Composable
@Preview
fun Splash() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = backColor(LocalContext.current)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_st),
            contentDescription = "",
            modifier = Modifier.padding(Dimens.LOGO_PADDING.dp)
        )
        SText(
            text = Strings.SHORTTASKS.s,
            fontSize = Dimens.TITLE.sp
        )
    }
}
