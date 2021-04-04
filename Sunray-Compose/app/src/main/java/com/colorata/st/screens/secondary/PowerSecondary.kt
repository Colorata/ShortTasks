package com.colorata.st.screens.secondary

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.colorata.st.extensions.ShowBubble
import com.colorata.st.extensions.presets.SButton
import com.colorata.st.extensions.presets.SText
import com.colorata.st.extensions.presets.Screen
import com.colorata.st.extensions.presets.TButtonDefault
import com.colorata.st.ui.theme.*

@ExperimentalAnimationApi
@Preview(showSystemUi = true)
@Composable
fun PowerSecondary(){
    Screen(
        titles = ScreenComponents.PowerHelp.titles,
        subTitles = ScreenComponents.PowerHelp.subTitles,
        icons = ScreenComponents.PowerHelp.icons,
        titleFontSize = SDimens.subTitle,
        hidden = listOf(
            { TButtonDefault() },
            { Help1() },
            { Help2() },
            { Help3() }
        )
    )

}


@Preview
@Composable
private fun Help1(){
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .padding(SDimens.largePadding)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        SButton(modifier = Modifier, text = Strings.go) {
            ShowBubble(context)
        }
    }
}

@Preview
@Composable
private fun Help2(){
    Row(
        modifier = Modifier
            .padding(SDimens.largePadding)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        SText(text = Strings.next)
    }
}

@Preview
@Composable
private fun Help3(){
    Row(
        modifier = Modifier
            .padding(SDimens.largePadding)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        SText(text = Strings.next)
    }
}