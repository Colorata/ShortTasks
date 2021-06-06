package com.colorata.st.extensions.presets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.colorata.st.ui.theme.*
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@Composable
fun Screen(
    titles: List<String>,
    subTitles: List<String>,
    icons: List<Int>,
    modifier: Modifier = Modifier,
    titleFontSize: TextUnit = SDimens.cardTitle,
    hidden: List<@Composable () -> Unit>
) {
    val context = LocalContext.current
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = backgroundColor())
    ) {
        val scope = rememberCoroutineScope()
        val state = rememberLazyListState()
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight(), state = state
        ) {
            itemsIndexed(items = titles, itemContent = { index, item ->
                when (item) {
                    titles[0] -> Title(title = item, subTitle = subTitles[titles.indexOf(item)])
                    titles[titles.lastIndex] -> {
                        var isVisible by remember { mutableStateOf(false)}
                        LaunchedEffect(key1 = true) {
                            isVisible = true
                        }
                        Column {
                            RelatedCard(
                                title = item,
                                modifier = Modifier.padding(
                                    bottom = SDimens.largePadding
                                ),
                                subTitle = subTitles[titles.indexOf(item)],
                                icon = icons[titles.indexOf(item)],
                                titleFontSize = titleFontSize,
                                scope = {
                                    scope.launch {
                                        state.animateScrollToItem(index)
                                    }
                                }
                            ) {
                                hidden[titles.indexOf(item)]()
                            }

                            AnimatedVisibility(visible = isVisible) {

                                SText(
                                    text = "You're on the end", modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            bottom = (SuperStore(context).catchInt(
                                                Strings.bottomSize,
                                                30
                                            ) * 4).dp
                                        )
                                )
                            }
                        }
                    }
                    else -> {
                        RelatedCard(
                            title = item,
                            subTitle = subTitles[titles.indexOf(item)],
                            icon = icons[titles.indexOf(item)],
                            titleFontSize = titleFontSize,
                            scope = {
                                scope.launch {
                                    state.animateScrollToItem(index)
                                }
                            }
                        ) {
                            hidden[titles.indexOf(item)]()
                        }
                    }
                }
            })
        }

    }
}


@ExperimentalAnimationApi
@Preview(
    name = "Screen",
    showBackground = true,
    showSystemUi = true,
    backgroundColor = 0xFF_FF_FF_FF
)
@Composable
fun ScreenDefault() {
    val hidden = listOf<@androidx.compose.runtime.Composable () -> Unit>({ TButtonDefault() },
        { TButtonDefault() },
        { TButtonDefault() },
        { TButtonDefault() })
    Screen(
        titles = ScreenComponents.MainScreen.titles,
        subTitles = ScreenComponents.MainScreen.subTitles,
        icons = ScreenComponents.MainScreen.icons,
        hidden = hidden
    )
}
