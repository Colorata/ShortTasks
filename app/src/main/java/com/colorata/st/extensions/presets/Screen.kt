package com.colorata.st.extensions.presets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.colorata.st.ui.theme.SDimens
import com.colorata.st.ui.theme.backgroundColor
import com.colorata.st.ui.theme.Strings
import com.colorata.st.ui.theme.SuperStore
import com.colorata.st.ui.theme.ScreenComponents
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
                        var isVisible by remember { mutableStateOf(false) }
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
                                        state.animateScrollToItem(index - 1)
                                    }
                                }
                            ) {
                                hidden[titles.indexOf(item)]()
                            }

                            AnimatedVisibility(visible = isVisible) {
                                SText(
                                    text = Strings.end, modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            bottom = (SuperStore(context).catchInt(
                                                Strings.bottomSize,
                                                30
                                            ) * 5).dp
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
                            titleFontSize = titleFontSize
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
    val hidden = listOf<@Composable () -> Unit>({ TButtonDefault() },
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
