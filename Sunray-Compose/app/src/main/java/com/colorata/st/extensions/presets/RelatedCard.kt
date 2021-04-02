package com.colorata.st.extensions.presets

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.colorata.st.R
import com.colorata.st.ui.theme.backColor
import com.colorata.st.ui.theme.buttonColor

@SuppressLint("CoroutineCreationDuringComposition")
@ExperimentalAnimationApi
@Composable
fun RelatedCard(titleFontSize: TextUnit = 24.sp, subTitleFontSize: TextUnit = 16.sp, title: String, subTitle: String, icon: Int, hidden: @Composable () -> Unit){

    var show by remember {
        mutableStateOf(false)
    }


    Card(shape = RoundedCornerShape(30.dp),
        border = BorderStroke(width = 2.dp, color = buttonColor(LocalContext.current)),
        backgroundColor = backColor(LocalContext.current),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Column(modifier = Modifier.clickable { show = !show }) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Box(contentAlignment = Alignment.Center) {

                    Image(
                        painter = painterResource(id = R.drawable.background),
                        contentDescription = "",
                        modifier = Modifier
                            .size(200.dp),
                        colorFilter = ColorFilter.tint(color = buttonColor(LocalContext.current))
                    )

                    Image(
                        painter = painterResource(icon),
                        contentDescription = "",
                        modifier = Modifier
                            .size(50.dp),
                        colorFilter = ColorFilter.tint(color = buttonColor(LocalContext.current))
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
                            .padding(20.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )

                    SText(
                        text = subTitle,
                        fontSize = subTitleFontSize,
                        modifier = Modifier
                            .padding(bottom = 20.dp, end = 20.dp, start = 20.dp)
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



@ExperimentalAnimationApi
@Preview(name = "Related Card")
@Composable
private fun RelatedCardPreview(){
    RelatedCard(
        title = "Card Title",
        subTitle = "Card SubTitle",
        icon = R.drawable.ic_outline_wb_sunny_24
    ) {
        SText(text = "Top", modifier = Modifier.padding(30.dp))
    }
}