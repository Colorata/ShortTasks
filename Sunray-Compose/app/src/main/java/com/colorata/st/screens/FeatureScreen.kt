package com.colorata.st.screens

import android.content.Context
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
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
import com.colorata.st.CurrentScreen
import com.colorata.st.extensions.getBottomNavigationHeight
import com.colorata.st.extensions.goToSecondary
import com.colorata.st.extensions.presets.*
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

@ExperimentalAnimationApi
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, group = "Hidden Content")
@Composable
fun WeatherDirectorContent(){
    val context = LocalContext.current
    val shared = context.getSharedPreferences(Strings.shared, Context.MODE_PRIVATE)

    var showDegrees by remember { mutableStateOf(false) }
    var showCity by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(SDimens.largePadding)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            SButton(
                modifier = Modifier.padding(end = SDimens.smallPadding),
                text = Strings.city
            ) { showCity = !showCity
                showDegrees = false}

            SButton(
                modifier = Modifier,
                text = Strings.degrees
            ) { showDegrees = !showDegrees
                showCity = false}
        }

        AnimatedVisibility(visible = showDegrees) {
            Column {
                MinDegrees()
                MaxDegrees()
            }
        }

        AnimatedVisibility(visible = showCity) {
            Column(modifier = Modifier.padding(vertical = SDimens.normalPadding)) {
                var state by remember {
                    mutableStateOf("")
                }
                SField(label = Strings.city) {
                    state = it.text
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = SDimens.normalPadding),
                    horizontalArrangement = Arrangement.End
                ) {
                    SButton(
                        text = Strings.save,
                        modifier = Modifier.padding(vertical = SDimens.normalPadding)
                    ) {
                        shared
                            .edit()
                            .putString(
                                Strings.city,
                                state
                            )
                            .apply()
                    }
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, group = "Hidden Content")
@Composable
fun PowerAssistantContent(){
    val context = LocalContext.current

    var addLinkState by remember { mutableStateOf(false) }
    Column(modifier = Modifier.padding(SDimens.largePadding)) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            SButton(modifier = Modifier.padding(end = SDimens.smallPadding), text = Strings.addLink) {
                addLinkState = !addLinkState
            }

            SButton(modifier = Modifier.padding(bottom = SDimens.smallPadding), text = Strings.addApp) {
                context.goToSecondary(CurrentScreen.ADD_APP)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            SButton(modifier = Modifier, text = Strings.help) {

            }
        }

        AnimatedVisibility(visible = addLinkState) {
            Column {
                SField(label = Strings.title, modifier = Modifier.padding(top = SDimens.normalPadding)) {

                }

                SField(label = Strings.link, modifier = Modifier.padding(top = SDimens.normalPadding)) {

                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    SButton(modifier = Modifier.padding(SDimens.normalPadding), text = Strings.save) {

                    }
                }
            }
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
    val context = LocalContext.current
    val shared = context.getSharedPreferences(Strings.shared, Context.MODE_PRIVATE)
    var sliderState by remember { mutableStateOf(shared.getInt(
        Strings.minDegrees,
        -50
    ).toFloat()) }

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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SDimens.normalPadding),
                horizontalArrangement = Arrangement.End
            ) {
                SButton(
                    text = Strings.save,
                    modifier = Modifier.padding(SDimens.normalPadding)
                ) {
                    shared
                        .edit()
                        .putInt(
                            Strings.minDegrees,
                            sliderState.toInt()
                        )
                        .apply()
                }
            }
        }
    }
}

@Composable
fun MaxDegrees() {
    val context = LocalContext.current
    val shared = context.getSharedPreferences(Strings.shared, Context.MODE_PRIVATE)
    var sliderState by remember { mutableStateOf(shared.getInt(
        Strings.maxDegrees,
        50
    ).toFloat()) }

    val sliderColors = SliderDefaults.colors(
        thumbColor = buttonColor(LocalContext.current),
        activeTrackColor = buttonColor(LocalContext.current)
    )

    SText(
        text = Strings.maxDegrees,
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SDimens.normalPadding),
                horizontalArrangement = Arrangement.End
            ) {
                SButton(
                    text = Strings.save,
                    modifier = Modifier.padding(SDimens.normalPadding)
                ) {
                    shared
                        .edit()
                        .putInt(
                            Strings.maxDegrees,
                            sliderState.toInt()
                        )
                        .apply()
                }
            }
        }
    }
}