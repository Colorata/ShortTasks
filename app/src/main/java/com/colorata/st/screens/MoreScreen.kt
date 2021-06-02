package com.colorata.st.screens

import android.content.Context
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.colorata.st.CurrentScreen
import com.colorata.st.extensions.getBottomNavigationHeight
import com.colorata.st.extensions.goToSecondary
import com.colorata.st.extensions.presets.SButton
import com.colorata.st.extensions.presets.SMessage
import com.colorata.st.extensions.presets.Screen
import com.colorata.st.extensions.presets.TButtonDefault
import com.colorata.st.ui.theme.SDimens
import com.colorata.st.ui.theme.ScreenComponents
import com.colorata.st.ui.theme.Strings

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
    val shared = LocalContext.current.getSharedPreferences(Strings.shared, Context.MODE_PRIVATE)
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
            shared.edit().clear().apply()
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, group = "Hidden Content")
@Composable
fun HelpContent() {
    Column(modifier = Modifier.padding(SDimens.largePadding)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            SButton(modifier = Modifier.padding(end = SDimens.smallPadding), text = Strings.power) {

            }

            SButton(text = Strings.weather) {

            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, group = "Hidden Content")
@Composable
fun AboutContent() {
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

            }

            SButton(text = Strings.donation) {

            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            SButton(text = Strings.version) {

            }
        }
    }
}