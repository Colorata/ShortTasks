package com.colorata.st.screens

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.colorata.st.extensions.presets.SButton
import com.colorata.st.extensions.presets.Screen
import com.colorata.st.extensions.presets.TButtonDefault
import com.colorata.st.ui.theme.ListComponents
import com.colorata.st.ui.theme.Strings

@ExperimentalAnimationApi
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MoreScreen() {
    Screen(
        titles = ListComponents.MORE_SCREEN.titles,
        subTitles = ListComponents.MORE_SCREEN.subTitles,
        icons = ListComponents.MORE_SCREEN.icons,
        hidden = listOf(
            { TButtonDefault() },
            { SettingsContent() },
            { HelpContent() },
            { AboutContent() }
        )
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, group = "Hidden Content")
@Composable
fun SettingsContent(){
    Row(
        modifier = Modifier
            .padding(30.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        SButton(modifier = Modifier, text = Strings.erase) {
            Log.d("Clicked", "Erase")
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, group = "Hidden Content")
@Composable
fun HelpContent(){
    Column(modifier = Modifier.padding(30.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            SButton(modifier = Modifier.padding(end = 10.dp, bottom = 10.dp), text = Strings.bubble) {
                Log.d("Clicked", "Bubble")
            }

            SButton(modifier = Modifier, text = Strings.power) {
                Log.d("Clicked", "Power")
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            SButton(modifier = Modifier, text = Strings.weather) {
                Log.d("Clicked", "Weather")
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, group = "Hidden Content")
@Composable
fun AboutContent(){
    Column(modifier = Modifier.padding(30.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            SButton(modifier = Modifier.padding(end = 10.dp, bottom = 10.dp), text = Strings.feedback) {
                Log.d("Clicked", "Feedback")
            }

            SButton(modifier = Modifier, text = Strings.donation) {
                Log.d("Clicked", "Donation")
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            SButton(modifier = Modifier, text = Strings.version) {
                Log.d("Clicked", "Weather")
            }
        }
    }
}