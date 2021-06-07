package com.colorata.st.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.colorata.st.CurrentScreen
import com.colorata.st.extensions.getBottomNavigationHeight
import com.colorata.st.extensions.goToSecondary
import com.colorata.st.extensions.presets.Screen
import com.colorata.st.extensions.presets.TButtonDefault
import com.colorata.st.extensions.presets.SButton
import com.colorata.st.extensions.presets.SField
import com.colorata.st.extensions.presets.SText
import com.colorata.st.ui.theme.Strings
import com.colorata.st.ui.theme.ScreenComponents
import com.colorata.st.ui.theme.SDimens
import com.colorata.st.ui.theme.SuperStore
import com.colorata.st.ui.theme.foregroundColor
import com.colorata.st.ui.theme.backgroundColor

@ExperimentalComposeUiApi
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

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, group = "Hidden Content")
@Composable
fun WeatherDirectorContent() {
    val context = LocalContext.current

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
            ) {
                showCity = !showCity
                showDegrees = false
            }

            SButton(
                modifier = Modifier,
                text = Strings.degrees
            ) {
                showDegrees = !showDegrees
                showCity = false
            }
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
                        SuperStore(context).drop(Strings.city, state)
                    }
                }
            }
        }
    }
}

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, group = "Hidden Content")
@Composable
fun PowerAssistantContent() {
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var link by remember { mutableStateOf("") }

    var addLinkState by remember { mutableStateOf(false) }
    Column(modifier = Modifier.padding(SDimens.largePadding)) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            SButton(
                modifier = Modifier.padding(end = SDimens.smallPadding),
                text = Strings.addLink
            ) {
                addLinkState = !addLinkState
            }

            SButton(
                modifier = Modifier.padding(bottom = SDimens.smallPadding),
                text = Strings.addApp
            ) {
                context.goToSecondary(CurrentScreen.ADD_APP)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            SButton(text = Strings.help) {

            }
        }

        AnimatedVisibility(visible = addLinkState) {
            Column {
                SField(
                    label = Strings.title,
                    modifier = Modifier.padding(top = SDimens.normalPadding),
                    imeAction = ImeAction.Next
                ) {
                    title = it.text
                }

                SField(
                    label = Strings.link,
                    modifier = Modifier.padding(top = SDimens.normalPadding)
                ) {
                    link = it.text
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    SButton(
                        modifier = Modifier.padding(SDimens.normalPadding),
                        text = Strings.save
                    ) {
                        if (!link.startsWith("http://") or !link.startsWith("https://")) link =
                            "https://$link"
                        SuperStore(context).drop(Strings.linkTitle, title)
                        SuperStore(context).drop(Strings.linkLink, link)
                    }
                }
            }
        }
    }
}

@Composable
fun MinDegrees() {
    val context = LocalContext.current
    var sliderState by remember {
        mutableStateOf(
            SuperStore(context).catchInt(Strings.minDegrees, -50).toFloat()
        )
    }

    val sliderColors = SliderDefaults.colors(
        thumbColor = foregroundColor(),
        activeTrackColor = foregroundColor()
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
            color = foregroundColor(),
        ),
        color = backgroundColor()
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
                    SuperStore(context).drop(Strings.minDegrees, sliderState.toInt())
                }
            }
        }
    }
}

@Composable
fun MaxDegrees() {
    val context = LocalContext.current
    var sliderState by remember {
        mutableStateOf(
            SuperStore(context).catchInt(Strings.maxDegrees, 50).toFloat()
        )
    }

    val sliderColors = SliderDefaults.colors(
        thumbColor = foregroundColor(),
        activeTrackColor = foregroundColor()
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
            color = foregroundColor(),
        ),
        color = backgroundColor()
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
                    SuperStore(context).drop(Strings.maxDegrees, sliderState.toInt())
                }
            }
        }
    }
}