package com.colorata.st.screens

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.colorata.st.extensions.ShowBubble
import com.colorata.st.extensions.getBottomNavigationHeight
import com.colorata.st.extensions.presets.SButton
import com.colorata.st.extensions.presets.Screen
import com.colorata.st.extensions.presets.TButtonDefault
import com.colorata.st.ui.theme.SDimens
import com.colorata.st.ui.theme.ScreenComponents
import com.colorata.st.ui.theme.Strings

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun FeatureScreen() {
    Screen(
        titles = ScreenComponents.FeaturesScreen.titles,
        subTitles = ScreenComponents.FeaturesScreen.subTitles,
        icons = ScreenComponents.FeaturesScreen.icons,
        modifier = Modifier.padding(bottom = getBottomNavigationHeight()),
        hidden = listOf(
            { TButtonDefault() },
            { BubbleManagerContent() },
            { WeatherDirectorContent() },
            { PowerAssistantContent() }
        )
    )
}


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, group = "Hidden Content")
@Composable
fun BubbleManagerContent(){

    val context = LocalContext.current
    Column(modifier = Modifier.padding(SDimens.largePadding)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            SButton(modifier = Modifier.padding(
                end = SDimens.smallPadding,
                bottom = SDimens.smallPadding
            ), text = Strings.position) {
                Log.d("Clicked", "Position")
            }

            SButton(modifier = Modifier, text = Strings.add) {
                Log.d("Clicked", "Add")
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            SButton(modifier = Modifier.padding(end = SDimens.smallPadding), text = Strings.enable) {
                Log.d("Clicked", "Enable")
                ShowBubble(context).show()
            }

            SButton(modifier = Modifier, text = Strings.help) {
                Log.d("Clicked", "Help")
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, group = "Hidden Content")
@Composable
fun WeatherDirectorContent(){
    Row(
        modifier = Modifier
            .padding(SDimens.largePadding)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        SButton(modifier = Modifier.padding(end = SDimens.smallPadding), text = Strings.city) {
            Log.d("Clicked", "City")
        }

        SButton(modifier = Modifier, text = Strings.help) {
            Log.d("Clicked", "Help")
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, group = "Hidden Content")
@Composable
fun PowerAssistantContent(){
    Row(
        modifier = Modifier
            .padding(SDimens.largePadding)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        SButton(modifier = Modifier.padding(end = SDimens.smallPadding), text = Strings.edit) {
            Log.d("Clicked", "Edit")
        }

        SButton(modifier = Modifier, text = Strings.help) {
            Log.d("Clicked", "Help")
        }
    }
}

//TODO: fix bug with LazyList in LazyList crush
/*
@ExperimentalFoundationApi
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun BubbleContent() {
    val context = LocalContext.current

    val interactions = mutableListOf(
        "Position", "Add", "Enable", "More"
    )
    LazyVerticalGrid(cells = GridCells.Fixed(2)) {
        items(interactions){ item ->
            SButton(modifier = Modifier.padding(10.dp), text = item) {
                Toast.makeText(context, "$item clicked", Toast.LENGTH_SHORT).show()
            }
        }
    }
}*/
