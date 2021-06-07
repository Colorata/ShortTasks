package com.colorata.st.extensions.presets

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.remember
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import com.colorata.st.R
import com.colorata.st.ui.theme.SDimens
import com.colorata.st.ui.theme.backgroundColor
import com.colorata.st.ui.theme.foregroundColor

@SuppressLint("CoroutineCreationDuringComposition")
@ExperimentalAnimationApi
@Composable
fun RelatedCard(
    modifier: Modifier = Modifier,
    titleFontSize: TextUnit = SDimens.cardTitle,
    subTitleFontSize: TextUnit = SDimens.subTitle,
    title: String,
    subTitle: String,
    scope: () -> Unit = { },
    icon: Int,

    hidden: @Composable () -> Unit
) {

    var show by remember {
        mutableStateOf(false)
    }


    var isVisible by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = true) {
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(700)) + expandVertically(animationSpec = tween(700)),
        modifier = modifier
    ) {
        Card(
            shape = RoundedCornerShape(SDimens.roundedCorner),
            border = BorderStroke(width = SDimens.borderWidth, color = foregroundColor()),
            backgroundColor = backgroundColor(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(SDimens.cardPadding)
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.clickable {
                        if (!show) scope()
                        show = !show
                    }
                ) {
                    Box(contentAlignment = Alignment.Center) {

                        Image(
                            painter = painterResource(id = R.drawable.background),
                            contentDescription = "",
                            modifier = Modifier
                                .size(SDimens.cardHeight),
                            colorFilter = ColorFilter.tint(color = foregroundColor())
                        )

                        Image(
                            painter = painterResource(icon),
                            contentDescription = "",
                            modifier = Modifier
                                .size(SDimens.postImageSize),
                            colorFilter = ColorFilter.tint(color = foregroundColor())
                        )
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top
                    ) {

                        SText(
                            text = title,
                            fontSize = titleFontSize,
                            modifier = Modifier
                                .padding(SDimens.normalPadding)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Start
                        )

                        SText(
                            text = subTitle,
                            fontSize = subTitleFontSize,
                            modifier = Modifier
                                .padding(
                                    bottom = SDimens.normalPadding,
                                    end = SDimens.normalPadding,
                                    start = SDimens.normalPadding
                                )
                                .fillMaxWidth(),
                            textAlign = TextAlign.Start
                        )
                    }
                }
                AnimatedVisibility(
                    visible = show
                ) {
                    hidden()
                }
            }
        }
    }
}


@ExperimentalAnimationApi
@Preview(name = "Related Card")
@Composable
private fun RelatedCardPreview() {
    RelatedCard(
        title = "Card Title",
        subTitle = "Card SubTitle",
        icon = R.drawable.ic_outline_wb_sunny_24
    ) {
        SText(text = "Top", modifier = Modifier.padding(SDimens.largePadding))
    }
}