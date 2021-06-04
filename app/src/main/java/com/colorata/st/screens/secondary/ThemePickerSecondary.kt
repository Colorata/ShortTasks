package com.colorata.st.screens.secondary

import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.colorata.st.extensions.presets.SText
import com.colorata.st.extensions.presets.Title
import com.colorata.st.ui.theme.*

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Preview(showSystemUi = true)
@Composable
fun ThemePickerSecondary() {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor())
    ) {

        val labels = mutableListOf("Title")
        var currentTheme by remember { mutableStateOf(SystemColor.BLACK) }
        SystemColor.values().forEach {
            labels.add(it.title)
        }

        val colors = mutableListOf(Pair(backgroundColor(), foregroundColor()))
        colors.addAll(getAllColors())
        LazyColumn(modifier = Modifier.fillMaxHeight()) {
            itemsIndexed(items = labels) { index, _ ->
                if (index == 0) {
                    Column {
                        Title(
                            title = Strings.themePicker,
                            subTitle = Strings.themePickerSubTitle
                        )
                        SText(
                            text = Strings.currentTheme,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = SDimens.normalPadding)
                        )
                        Crossfade(
                            targetState = currentTheme,
                            animationSpec = tween(100),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = SDimens.normalPadding)
                        ) {
                            SText(text = it.title, modifier = Modifier.fillMaxWidth())
                        }
                    }
                } else {
                    Card(
                        shape = RoundedCornerShape(SDimens.roundedCorner),
                        border = BorderStroke(
                            width = SDimens.borderWidth,
                            color = foregroundColor()
                        ),
                        backgroundColor = backgroundColor(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(SDimens.cardPadding),
                        onClick = {
                            currentTheme = SystemColor.values().find { it.title == labels[index]}!!

                            if (currentTheme == SystemColor.BLACK) SuperStore(context).drop(
                                Strings.autoDetect,
                                true
                            )
                            else {
                                SuperStore(context).drop(Strings.autoDetect, false)

                                SuperStore(context).drop(
                                    mutableListOf(
                                        Pair(
                                            Strings.systemColor,
                                            currentTheme.id
                                        ),
                                        Pair(
                                            Strings.primaryInt,
                                            if (!SuperStore(context).catchBoolean(Strings.nightMode)) currentTheme.secondaryHex.toIntColor()
                                            else currentTheme.primaryHex.toIntColor()
                                        ),
                                        Pair(
                                            Strings.secondaryInt,
                                            if (!SuperStore(context).catchBoolean(Strings.nightMode)) currentTheme.primaryHex.toIntColor()
                                            else currentTheme.secondaryHex.toIntColor()
                                        ),
                                        Pair(
                                            Strings.controlColor,
                                            currentTheme.secondaryHex.toIntColor()
                                        )
                                    )
                                )
                                context.setSystemColor()
                            }


                        }
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(SDimens.normalPadding)
                                .weight(2f),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Card(
                                    shape = RoundedCornerShape(50),
                                    border = BorderStroke(
                                        width = 3.dp,
                                        color = colors[index].second
                                    ),
                                    modifier = Modifier
                                        .size(50.dp),
                                    backgroundColor = colors[index].first
                                ) { }
                                SText(
                                    text = labels[index],
                                    fontSize = SDimens.subTitle,
                                    modifier = Modifier
                                        .padding(horizontal = SDimens.largePadding)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}