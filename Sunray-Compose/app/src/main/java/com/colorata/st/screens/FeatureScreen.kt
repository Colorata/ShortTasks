package com.colorata.st.screens

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.colorata.st.extensions.getBottomNavigationHeight
import com.colorata.st.extensions.presets.SButton
import com.colorata.st.extensions.presets.SText
import com.colorata.st.extensions.presets.Screen
import com.colorata.st.extensions.presets.TButtonDefault
import com.colorata.st.ui.theme.*

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
            { WeatherDirectorContent() },
            { PowerAssistantContent() }
        )
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, group = "Hidden Content")
@Composable
fun WeatherDirectorContent(){
    Column(modifier = Modifier.padding(SDimens.largePadding)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            SButton(modifier = Modifier.padding(end = SDimens.smallPadding), text = Strings.city) {

                Log.d("Clicked", "City")

            }

            SButton(modifier = Modifier, text = Strings.help) {

                Log.d("Clicked", "Help")

            }
        }

        MinDegrees()

        MaxDegrees()
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


@Composable
fun MinDegrees() {
    var sliderState by remember { mutableStateOf(-50f) }

    val sliderColors = SliderDefaults.colors(
        thumbColor = buttonColor(LocalContext.current),
        activeTrackColor = buttonColor(LocalContext.current)
    )

    SText(
        text = Strings.minDegrees,
        modifier = Modifier
            .fillMaxWidth()
            .padding(SDimens.normalPadding),
        textAlign = TextAlign.Center
    )

    Surface(
        shape = RoundedCornerShape(SDimens.roundedCorner),
        border = BorderStroke(
            SDimens.borderWidth,
            color = buttonColor(LocalContext.current),
        ),
        color = backColor(LocalContext.current)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(SDimens.normalPadding),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SText(
                    text = Strings.minMinDegrees,
                    fontSize = SDimens.buttonText
                )
                SText(
                    text = "${sliderState.toInt()}℃",
                    fontSize = SDimens.buttonText
                )
                SText(
                    text = Strings.minMaxDegrees,
                    fontSize = SDimens.buttonText
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SDimens.normalPadding),
                horizontalArrangement = Arrangement.End
            ) {
                Slider(
                    value = sliderState.toInt().toFloat(),
                    steps = 0,
                    valueRange = -100f..0f,
                    onValueChange = { newValue ->
                        sliderState = newValue
                    },
                    colors = sliderColors
                )
            }
        }
    }
}

@Composable
fun MaxDegrees() {
    var sliderState by remember { mutableStateOf(50f) }

    val sliderColors = SliderDefaults.colors(
        thumbColor = buttonColor(LocalContext.current),
        activeTrackColor = buttonColor(LocalContext.current)
    )

    SText(
        text = Strings.minDegrees,
        modifier = Modifier
            .fillMaxWidth()
            .padding(SDimens.normalPadding),
        textAlign = TextAlign.Center
    )

    Surface(
        shape = RoundedCornerShape(SDimens.roundedCorner),
        border = BorderStroke(
            SDimens.borderWidth,
            color = buttonColor(LocalContext.current),
        ),
        color = backColor(LocalContext.current)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(SDimens.normalPadding),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SText(
                    text = Strings.maxMinDegrees,
                    fontSize = SDimens.buttonText
                )
                SText(
                    text = "${sliderState.toInt()}℃",
                    fontSize = SDimens.buttonText
                )
                SText(
                    text = Strings.maxMaxDegrees,
                    fontSize = SDimens.buttonText
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SDimens.normalPadding),
                horizontalArrangement = Arrangement.End
            ) {
                Slider(
                    value = sliderState.toInt().toFloat(),
                    steps = 0,
                    valueRange = 0f..100f,
                    onValueChange = { newValue ->
                        sliderState = newValue
                    },
                    colors = sliderColors
                )
            }
        }
    }
}