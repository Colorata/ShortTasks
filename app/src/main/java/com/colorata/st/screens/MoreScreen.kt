package com.colorata.st.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.colorata.st.CurrentScreen
import com.colorata.st.extensions.getBottomNavigationHeight
import com.colorata.st.extensions.goToSecondary
import com.colorata.st.extensions.isDeviceRooted
import com.colorata.st.extensions.presets.SButton
import com.colorata.st.extensions.presets.SMessage
import com.colorata.st.extensions.presets.Screen
import com.colorata.st.extensions.presets.TButtonDefault
import com.colorata.st.ui.theme.SDimens
import com.colorata.st.ui.theme.ScreenComponents
import com.colorata.st.ui.theme.Strings
import com.colorata.st.ui.theme.SuperStore

@ExperimentalAnimationApi
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MoreScreen() {
    Screen(
        titles = ScreenComponents.MoreScreen.titles,
        subTitles = ScreenComponents.MoreScreen.subTitles,
        icons = ScreenComponents.MoreScreen.icons,
        modifier = Modifier.padding(bottom = getBottomNavigationHeight()),
        hidden = listOf(
            { TButtonDefault() },
            { SettingsContent() },
            { HelpContent() },
            { AboutContent() }
        )
    )
}

@ExperimentalAnimationApi
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, group = "Hidden Content")
@Composable
fun SettingsContent() {
    val context = LocalContext.current

    var isClearVisible by remember { mutableStateOf(false) }
    Column(modifier = Modifier.padding(SDimens.largePadding)) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            SButton(
                modifier = Modifier.padding(end = SDimens.smallPadding),
                text = Strings.erase
            ) {
                isClearVisible = !isClearVisible
            }

            SButton(text = Strings.theme) {
                context.goToSecondary(CurrentScreen.THEME_PICKER)
            }
        }
        SMessage(visible = isClearVisible, text = Strings.clearSubTitle) {
            SuperStore(context).empty()
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, group = "Hidden Content")
@Composable
fun HelpContent() {
    val context = LocalContext.current
    Column(modifier = Modifier.padding(SDimens.largePadding)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            SButton(text = Strings.power) {
                context.goToSecondary(CurrentScreen.POWER)
            }
        }
    }
}

@ExperimentalAnimationApi
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, group = "Hidden Content")
@Composable
fun AboutContent() {
    val context = LocalContext.current
    var isVisible by remember { mutableStateOf(false) }
    var counter by remember { mutableStateOf(0) }
    var text by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(SDimens.largePadding)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            SButton(
                modifier = Modifier.padding(
                    end = SDimens.smallPadding,
                    bottom = SDimens.smallPadding
                ), text = Strings.feedback
            ) {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/Colorata/ShortTasks/issues")
                    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }

            SButton(text = Strings.donation) {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://qiwi.com/n/COLORATA")
                    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            SButton(
                text = Strings.version,
                modifier = Modifier.padding(end = SDimens.smallPadding)
            ) {
                counter += 1
                when (counter) {
                    10 -> {
                        isVisible = true
                        text = if (isDeviceRooted()) Strings.whyRoot else Strings.egg1
                        if (isDeviceRooted())
                            SuperStore(context).drop(Strings.hasRoot, true)
                    }
                    100 -> {
                        isVisible = true
                        text = Strings.egg2
                    }
                    1000 -> {
                        isVisible = true
                        text = Strings.egg3
                    }
                    10000 -> {
                        isVisible = true
                        text = Strings.egg4
                    }
                    else -> {
                        text = Strings.currentVersion
                        isVisible = true
                    }
                }
            }

            SButton(text = Strings.developer) {
                text = Strings.currentDeveloper
                isVisible = true
            }
        }

        SMessage(visible = isVisible, text = text) {
            isVisible = false
        }

    }
}