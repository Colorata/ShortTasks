package com.colorata.st.extensions.presets

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import com.colorata.st.ui.theme.SDimens
import com.colorata.st.ui.theme.ScreenComponents
import com.colorata.st.ui.theme.backgroundColor

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
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = backgroundColor())
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxHeight()) {
            items(items = titles, itemContent = { item ->
                when (item) {
                    titles[0] -> Title(title = item, subTitle = subTitles[titles.indexOf(item)])
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
@Preview(name = "Screen", showBackground = true, showSystemUi = true, backgroundColor = 0xFF_FF_FF_FF)
@Composable
fun ScreenDefault(){
    val hidden = listOf<@androidx.compose.runtime.Composable () -> Unit> ({ TButtonDefault() }, { TButtonDefault()}, { TButtonDefault()}, { TButtonDefault()})
    Screen(
        titles = ScreenComponents.MainScreen.titles,
        subTitles = ScreenComponents.MainScreen.subTitles,
        icons = ScreenComponents.MainScreen.icons,
        hidden = hidden
    )
}
