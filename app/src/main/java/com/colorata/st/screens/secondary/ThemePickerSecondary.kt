package com.colorata.st.screens.secondary

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.colorata.st.R
import com.colorata.st.extensions.presets.SText
import com.colorata.st.extensions.presets.Title
import com.colorata.st.ui.theme.*
import com.colorata.st.ui.theme.Strings

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Preview(showSystemUi = true)
@Composable
fun ThemePickerSecondary() {
    val state by remember { mutableStateOf(ScrollableState { 0f }) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor())
            .scrollable(state = state, orientation = Orientation.Vertical)
    ) {

        val labels = mutableListOf("Title")
        SystemColor.values().forEach {
            labels.add(it.title)
        }
        val colors = mutableListOf(Pair(backgroundColor(), foregroundColor()))
        colors.addAll(getAllColors())

        LazyColumn(modifier = Modifier.fillMaxHeight()) {
            itemsIndexed(items = labels) { index, _ ->
                var isShowing by remember { mutableStateOf(false) }
                if (index == 0) {
                    Title(
                        title = Strings.themePicker,
                        subTitle = Strings.themePickerSubTitle
                    )
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
                            .padding(SDimens.cardPadding)
                    ) {
                        Row(modifier = Modifier
                            .clickable { isShowing = !isShowing }
                            .padding(SDimens.normalPadding)
                            .weight(2f),
                            horizontalArrangement = Arrangement.SpaceBetween) {
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
                            AnimatedVisibility(
                                visible = isShowing,
                                modifier = Modifier.weight(1f)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_outline_check_24),
                                    contentDescription = "",
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}