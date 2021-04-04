package com.colorata.st.screens.secondary

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.colorata.st.extensions.presets.SControl
import com.colorata.st.extensions.presets.Title
import com.colorata.st.ui.theme.BubbleElements
import com.colorata.st.ui.theme.Strings
import com.colorata.st.ui.theme.backColor

@ExperimentalFoundationApi
@Preview(showSystemUi = true)
@Composable
fun BubblePositionSecondary() {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = backColor(LocalContext.current))) {

        Title(title = Strings.position, subTitle = Strings.positionScreenSubTitle)


        LazyVerticalGrid(
            cells = GridCells.Fixed(3),
            content = {
                items(BubbleElements.titles.size) { index ->
                    SControl(
                        title = BubbleElements.titles[index],
                        icon = BubbleElements.icons[index]
                    )
                }
            }
        )
    }
}